package fun.zhub.ppeng.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.dto.AddUserMessageDTO;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.entity.RecipeCensor;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.feign.MessageService;
import fun.zhub.ppeng.mapper.RecipeCensorMapper;
import fun.zhub.ppeng.service.RecipeCensorService;
import fun.zhub.ppeng.service.RecipeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static fun.zhub.ppeng.constant.RedisConstants.MAP_RECIPE_ID_KEY;
import static fun.zhub.ppeng.constant.RedisConstants.MAP_RECIPE_ID_TTL;
import static fun.zhub.ppeng.constant.SystemConstants.PPENG_RECIPE_APPEAL_URL;

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

    @Resource
    private MessageService messageService;

    @Resource
    private RecipeService recipeService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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

    /**
     * 发送违规通知
     *
     * @param userId       userId
     * @param recipeId     菜谱id
     * @param censorState  审核人员标识 1，机器，2人工，3人工复审
     * @param censorResult 审核结果
     */
    @Override
    public void sendViolationNotification(Long userId, Long recipeId, Integer censorState, String censorResult) {
        // 获取菜谱内容
        Recipe recipe = Optional.ofNullable(recipeService.getById(recipeId))
                .orElseThrow(() -> new BusinessException(ResponseStatus.HTTP_STATUS_500, "菜谱不存在"));


        String title = "菜谱违规通知";
        String content;
        String sign = null;
        if (censorState == 3) {
            content = "尊敬的用户，您的菜谱(标题为" + recipe.getTitle() + ")存在违规内容，违规原因是" + censorResult + "。结合多次审核结果，我们不再接受关于该菜谱的申述处理，并永久删除该菜谱。感谢您的配合！";
        } else {
            /*
             * 获取人工复审链接
             */
            // 随机生成sign
            sign = IdUtil.fastSimpleUUID();
            // 生成链接
            String url = PPENG_RECIPE_APPEAL_URL + "?sign=" + sign;

            content = "尊敬的用户，您的菜谱( 标题为" + recipe.getTitle() + " )存在违规内容，违规原因是" + censorResult + "。如果你对此有异议，请点击下方链接(有效期为7天)提交人工复审，我们将在7个工作日回复您。感谢您的配合！\n" + url;
        }
        AddUserMessageDTO addUserMessageDTO = new AddUserMessageDTO(userId, title, content);

        Boolean a = messageService.addMeaasge(addUserMessageDTO);

        if (BooleanUtil.isFalse(a)) {
            log.error("发送({})违规通知失败", userId);
            throw new BusinessException(fun.zhub.ppeng.common.ResponseStatus.HTTP_STATUS_500, "发送违规通知失败");
        }

        // 当发送消息成功后才将sign写入redis
        if (sign != null) {
            stringRedisTemplate.opsForValue().set(MAP_RECIPE_ID_KEY + sign, String.valueOf(recipeId), MAP_RECIPE_ID_TTL, TimeUnit.DAYS);
        }
    }

    /**
     * 发送审核通过通知
     *
     * @param userId   userId
     * @param recipeId 菜谱id
     */
    @Override
    public void sendApprovalNotification(Long userId, Long recipeId) {
        // 获取菜谱内容
        Recipe recipe = Optional.ofNullable(recipeService.getById(recipeId))
                .orElseThrow(() -> new BusinessException(ResponseStatus.HTTP_STATUS_500, "菜谱不存在"));

        String title = "菜谱审核通过通知";
        String content = "尊敬的用户，恭喜您！您的菜谱(标题为" + recipe.getTitle() + ")已通过审核，符合我们的社区标准和规定。感谢您的分享和支持！继续创作精彩的菜谱，为我们的社区带来更多美味的享受吧！祝您继续成功！";

        AddUserMessageDTO addUserMessageDTO = new AddUserMessageDTO(userId, title, content);

        Boolean a = messageService.addMeaasge(addUserMessageDTO);

        if (BooleanUtil.isFalse(a)) {
            log.error("发送({})违规通知失败", userId);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "发送违规通知失败");
        }
    }


}
