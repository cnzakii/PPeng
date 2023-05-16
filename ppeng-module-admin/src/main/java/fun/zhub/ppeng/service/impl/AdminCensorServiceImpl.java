package fun.zhub.ppeng.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import fun.zhub.ppeng.common.PageBean;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.ManualCensorDTO;
import fun.zhub.ppeng.dto.RecipeCensorResultDTO;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.entity.RecipeCensor;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.feign.RecipeCensorService;
import fun.zhub.ppeng.feign.RecipeService;
import fun.zhub.ppeng.service.AdminCensorService;
import fun.zhub.ppeng.service.AdminService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static fun.zhub.ppeng.common.ResponseStatus.HTTP_STATUS_500;
import static fun.zhub.ppeng.common.ResponseStatus.SUCCESS;

/**
 * AdminCensorService interface 实现类
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
@Service
@Slf4j
public class AdminCensorServiceImpl implements AdminCensorService {

    @Resource
    private RecipeCensorService recipeCensorService;

    @Resource
    private RecipeService recipeService;

    @Resource
    private AdminService adminService;


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 实现 获取举报的菜谱列表
     *
     * @param timestamp 最后一篇菜谱的时间戳
     * @param pageSize  一页所呈现的信息数
     * @param key       Redis 的key
     * @return list
     */
    @Override
    public PageBean<ManualCensorDTO> getReportedRecipeList(Long timestamp, Integer pageSize, String key) {
        // 获取Zset集合，并从小到大排序
        Set<TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, timestamp, Long.MAX_VALUE, 0, pageSize);
        // 为null则直接返回
        if (CollUtil.isEmpty(typedTuples)) {
            return null;
        }

        // 初始化成当前时间戳
        long lastTimestamp = System.currentTimeMillis();
        List<Long> idList = new ArrayList<>(typedTuples.size());
        // 获取id列表和最大时间戳
        for (TypedTuple<String> typedTuple : typedTuples) {
            // 获取分数
            lastTimestamp = Objects.requireNonNull(typedTuple.getScore()).longValue();
            idList.add(Long.valueOf(Objects.requireNonNull(typedTuple.getValue())));
        }


        // 根据id获取recipe和recipeCensor
        List<ManualCensorDTO> list = idList.stream()
                .map(id -> {
                    // 根据id查找recipe 和 recipeCensor
                    Recipe recipe = recipeService.queryRecipeById(id);
                    RecipeCensor recipeCensor = recipeCensorService.getRecipeCensorById(id);
                    // 只有当recipe 和 recipeCensor都存在时，才进行bean对象的拷贝
                    if (BeanUtil.isNotEmpty(recipeCensor) && BeanUtil.isNotEmpty(recipe)) {
                        ManualCensorDTO manualCensorDTO = BeanUtil.copyProperties(recipe, ManualCensorDTO.class);
                        BeanUtil.copyProperties(recipeCensor, manualCensorDTO);
                        return manualCensorDTO;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();


        return new PageBean<>(list, lastTimestamp);
    }

    /**
     * 实现审核结果的处理
     *
     * @param adminId  管理员id
     * @param recipeId 菜谱id
     * @param result   审核结果
     * @param reason   判定原因
     * @param isNotify 是否通知用户
     * @param key      Redis 的key
     */
    @Override
    public void handlerRecipeCensorResult(Long adminId, Long recipeId, Integer result, String reason, Integer isNotify, String key) {
        /*
         * 调用接口，查看是第几次审核
         */
        RecipeCensor recipeCensor = recipeCensorService.getRecipeCensorById(recipeId);

        int censorState;
        if (recipeCensor.getSecondManualCensorId() != null) {
            // 如果已经人工审核两次，则抛出异常
            throw new BusinessException(HTTP_STATUS_500, "超出审核次数");
        } else if (recipeCensor.getManualCensorId() == null) {
            // 说明是第一次人工审核
            censorState = 1;
        } else {
            // 说明是第二次人工审核
            censorState = 2;
        }


        RecipeCensorResultDTO censorResultDTO = new RecipeCensorResultDTO(recipeId, censorState, reason, adminId, LocalDateTime.now(), isNotify);

        // 根据不同result调用不同接口
        ResponseResult<String> response;
        if (result == 0) {
            // 审核通过调用放行接口
            response = recipeCensorService.setaccessible(censorResultDTO);
        } else {
            // 审核不通过调用封禁接口
            response = recipeCensorService.setInaccessible(censorResultDTO);
        }

        // 查看调用结果
        if (!StrUtil.equals(response.getStatus(), SUCCESS.getResponseCode())) {
            // 调用失败，则抛出异常
            log.error("调用recipeCensorService接口失败===》ResponseCode:{} Exception:{}", response.getStatus(), response.getMessage());
            throw new BusinessException(HTTP_STATUS_500, "处理审核结果失败");
        }

        // 将其从相应的redis集合中删除
        stringRedisTemplate.opsForZSet().remove(key, recipeId);

        // 更新管理员信息
        adminService.updateAdminTaskNum(adminId, result);

    }


}
