package fun.zhub.ppeng.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.zhub.ppeng.common.ResponseResult;
import com.zhub.ppeng.common.ResponseStatus;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.handler.BadContentHandler;
import fun.zhub.ppeng.service.ContentCensorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


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
    private BadContentHandler badContentHandler;

    /**
     * 实现用户昵称的审核
     *
     * @param userId   id
     * @param nickName 昵称
     */
    @Override
    public void censorNickName(Long userId, String nickName) {
        JSONObject jsonObject = client.textCensorUserDefined(nickName);

        // 判断是否合规
        String conclusion = jsonObject.get("conclusion").toString();

        log.debug("昵称内容审核结果===》{}", jsonObject);

        if (StrUtil.equals(conclusion, "不合规")) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            JSONObject one = jsonArray.getJSONObject(0);
            String msg = one.get("msg").toString();

            log.info("用户({})昵称违规===》{}", userId, msg);

            /*
             *TODO 可以调用信息模块，告知用户违规原因
             */


            // 利用OpenFeign调用修改昵称接口，修改昵称为： 违规昵称_5fsdfsfdf
            ResponseResult<String> result = badContentHandler.handleBadNickName(userId);

            // 判断是否调用成功
            if (StrUtil.equals(result.getStatus(), ResponseStatus.HTTP_STATUS_500.getResponseCode())) {
                log.error("修改{}违规昵称失败", userId);
                throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "修改违规昵称失败");
            }

        } else {
            if (!StrUtil.equals(conclusion, "合规")) {
                log.error("调用百度内容审核接口失败===>{}", jsonObject);
                throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "调用接口失败");
            }
            log.info("{}昵称({})审核通过", userId, nickName);
        }

    }

    /**
     * 实现菜谱内容的审核
     *
     * @param recipeId 菜谱Id
     * @param text     菜谱内容
     */
    @Override
    public void censorRecipeText(Long recipeId, String text) {
        /*
         * TODO 实现菜谱内容的审核的业务逻辑
         */

    }
}
