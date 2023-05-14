package fun.zhub.ppeng.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.dto.RecipeCensorResultDTO;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.feign.RecipeCensorService;
import fun.zhub.ppeng.feign.UserService;
import fun.zhub.ppeng.service.ContentCensorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fun.zhub.ppeng.constant.SystemConstants.FILE_ROOT_PATH;


/**
 * <p>
 * 利用百度API对内容进行审核
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-14
 **/
@Service
@Slf4j
public class ContentCensorServiceImpl implements ContentCensorService {

    @Resource
    private AipContentCensor client;

    @Resource
    private UserService userService;


    @Resource
    private RecipeCensorService recipeService;


    /**
     * 实现用户昵称的审核
     *
     * @param userId   id
     * @param nickName 昵称
     */
    @Override
    public void censorNickName(Long userId, String nickName) {
        JSONObject response = client.textCensorUserDefined(nickName);
        String msg = getMsgFromResponse(response);

        // 不存在违规消息，则直接返回
        if (StrUtil.isEmpty(msg)) {
            log.info("{}昵称({})审核通过", userId, nickName);
            return;
        }

        log.info("用户({})昵称违规===》{}", userId, msg);

        // 利用OpenFeign调用修改昵称接口，修改昵称为： 违规昵称_5fsdfsfdf ，并通知用户
        ResponseResult<String> result = userService.handleBadNickName(userId);

        if (!StrUtil.equals(result.getStatus(), ResponseStatus.SUCCESS.getResponseCode())) {
            log.error("修改{}违规昵称失败", userId);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "修改违规昵称失败");
        }

    }


    /**
     * 实现用户头像审核
     *
     * @param userId 用户id
     * @param path   头像地址
     */
    @Override
    public void censorUserIcon(Long userId, String path) {
        JSONObject response = client.imageCensorUserDefined(FILE_ROOT_PATH + path, EImgType.FILE, null);

        String msg = getMsgFromResponse(response);

        if (StrUtil.isEmpty(msg)) {
            log.info("用户({})头像({})审核通过", userId, path);
            return;
        }


        log.info("用户({})头像违规===》{}", userId, msg);
        /*
         * TODO 服务调用，将icon转换成特定头像
         */


    }

    /**
     * 实现菜谱图文审核
     *
     * @param recipeId 菜谱id
     * @param data     数据
     */
    @Override
    public void censorRecipeImages(Long recipeId, String[] data) {
        String title = data[0];
        String content = data[1];
        String[] images = data[2].split(",");

        // 标记是否违规
        boolean flag = false;

        // 审核标题+内容
        JSONObject titleResponse = client.textCensorUserDefined(title + "\n" + content);

        String msg = getMsgFromResponse(titleResponse);

        // 只要有一项违规，就不再继续审核
        if (StrUtil.isNotEmpty(msg)) {
            log.info("菜谱({})标题违规===》{}", recipeId, msg);
            msg = "内容: " + msg;
            flag = true;
        } else {
            // 审核图片集
            msg = Arrays.stream(images)
                    .map(path -> client.imageCensorUserDefined(FILE_ROOT_PATH + path, EImgType.FILE, null))
                    .map(this::getMsgFromResponse)
                    .filter(StrUtil::isNotEmpty)
                    .collect(Collectors.joining());
            if (StrUtil.isNotEmpty(msg)) {
                log.info("菜谱({})图片违规===》{}", recipeId, msg);
                msg = "图片: " + msg;
                flag = true;
            }
        }

        if (flag) {
            /*
             * 调用OpenFeign，将该菜谱加入黑名单，允许用户申述，交由人工审核
             */
            recipeService.setInaccessible(new RecipeCensorResultDTO(recipeId, 1, msg, null, LocalDateTime.now()));

        } else {
            // 审核通过，记录审核结果并更新审核状态
            recipeService.setaccessible(new RecipeCensorResultDTO(recipeId, 1, msg, null, LocalDateTime.now()));
        }


    }


    /**
     * 实现审核菜谱视频
     *
     * @param recipeId 菜谱id
     * @param data     数据
     */
    @Override
    public void censorRecipeVideo(Long recipeId, String[] data) {
        String title = data[0];
        String videoUrl = data[1];

        // 标记是否违规
        boolean flag = false;

        // 先审核标题
        JSONObject titleResponse = client.textCensorUserDefined(title);
        String msg = getMsgFromResponse(titleResponse);

        // 只要有一项违规，就不再继续审核
        if (StrUtil.isNotEmpty(msg)) {
            log.info("菜谱({})标题违规===》{}", recipeId, msg);
            msg = "标题: " + msg;
            flag = true;
        } else {
            // 审核视频
            JSONObject response = client.videoCensorUserDefined(title, videoUrl, String.valueOf(recipeId), null);
            // 判断是否合规
            String conclusion;
            try {
                conclusion = response.getString("conclusion");
            } catch (JSONException e) {
                log.error("调用百度内容审核接口失败===>{}", response);
                throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "调用接口失败");
            }

            if (StrUtil.equals(conclusion, "不合规") || StrUtil.equals(conclusion, "疑似")) {
                msg = response.getJSONArray("conclusionTypeGroupInfos").getJSONObject(0).getString("msg");

                log.info("菜谱({})视频违规===》{}", recipeId, msg);
                msg = "视频: " + msg;
                flag = true;
            }
        }

        if (flag) {
            /*
             *  调用OpenFeign，将该菜谱加入黑名单，允许用户申述，交由人工审核
             */
            recipeService.setInaccessible(new RecipeCensorResultDTO(recipeId, 1, msg, null, LocalDateTime.now()));
        } else {
            // 审核通过，记录审核结果并更新审核状态
            recipeService.setaccessible(new RecipeCensorResultDTO(recipeId, 1, msg, null, LocalDateTime.now()));
        }


    }


    /**
     * 实现 通用 response 请求处理
     *
     * @param response response
     * @return 不合规，疑似返回信息，如果合规，返回null
     */
    @Override
    public String getMsgFromResponse(JSONObject response) {
        String conclusion;
        try {
            conclusion = response.getString("conclusion");
        } catch (JSONException e) {
            log.error("调用百度内容审核接口失败===>{}", response);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "调用接口失败");
        }

        if (StrUtil.equals(conclusion, "不合规") || StrUtil.equals(conclusion, "疑似")) {
            JSONArray jsonArray = response.getJSONArray("data");
            List<String> msg = Stream.iterate(0, i -> i < jsonArray.length(), i -> i + 1)
                    .map(i -> jsonArray.getJSONObject(i).getString("msg"))
                    .toList();
            return String.join(",", msg);
        }
        return null;
    }


}
