package fun.zhub.ppeng.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.entity.RecipeCensor;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.mapper.RecipeCensorMapper;
import fun.zhub.ppeng.service.RecipeCensorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-04-26
 */
@Service
@Slf4j
public class RecipeCensorServiceImpl extends ServiceImpl<RecipeCensorMapper, RecipeCensor> implements RecipeCensorService {

    @Resource
    private RecipeCensorMapper recipeCensorMapper;

    /**
     * 实现审核结果的保存
     *
     * @param recipeId     菜谱id
     * @param censorResult 审核结果
     * @param censorId     审核人员id-机器审核则不填写
     * @param censorTime   审核时间
     * @param censorState  审核人员标识 1，机器，2人工，3人工复审
     */
    @Override
    public void saveCensorResult(Long recipeId, String censorResult, Long censorId, LocalDateTime censorTime, Integer censorState) {
        RecipeCensor recipeCensor = recipeCensorMapper.selectOne(new LambdaQueryWrapper<RecipeCensor>().eq(RecipeCensor::getRecipeId, recipeId));

        // 如果不存在则创建
        if (BeanUtil.isEmpty(recipeCensor)) {
            recipeCensor = new RecipeCensor();
            recipeCensor.setRecipeId(recipeId);
            int i = recipeCensorMapper.insert(recipeCensor);
            if (i == 0) {
                log.error("创建菜谱({})审核结果失败", recipeId);
                throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "创建菜谱审核结果失败");
            }
        }

        switch (censorState) {
            case 1 -> {
                recipeCensor.setAutoCensorResult(censorResult);
                recipeCensor.setAutoCensorTime(censorTime);
            }
            case 2 -> {
                recipeCensor.setManualCensorResult(censorResult);
                recipeCensor.setManualCensorId(censorId);
                recipeCensor.setManualCensorTime(censorTime);
            }
            case 3 -> {
                recipeCensor.setSecondManualCensorResult(censorResult);
                recipeCensor.setSecondManualCensorId(censorId);
                recipeCensor.setSecondManualCensorTime(censorTime);
            }
        }

        int i = recipeCensorMapper.updateById(recipeCensor);

        if (i != 1) {
            log.error("更新菜谱({})审核结果失败", recipeId);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "更新菜谱审核结果失败");
        }


    }


}
