package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.entity.RecipeCensor;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Zaki
 * @since 2023-04-26
 */
public interface RecipeCensorService extends IService<RecipeCensor> {

    /**
     * 保存审核结果
     *
     * @param recipeId     菜谱id
     * @param censorResult 审核结果
     * @param censorId     审核人员id-机器审核则不填写
     * @param censorTime   审核时间
     * @param censorState  审核人员标识 1，机器，2人工，3人工复审
     */
    void saveCensorResult(Long recipeId, String censorResult, Long censorId, LocalDateTime censorTime, Integer censorState);

    /**
     * 发送违规通知
     *
     * @param userId       userId
     * @param recipeId     菜谱id
     * @param censorState  审核人员标识 1，机器，2人工，3人工复审
     * @param censorResult 审核结果
     */
    void sendViolationNotification(Long userId, Long recipeId, Integer censorState, String censorResult);

    /**
     * 发送审核通过通知
     *
     * @param userId   userId
     * @param recipeId 菜谱id
     */
    void sendApprovalNotification(Long userId, Long recipeId);
}
