package fun.zhub.ppeng.service;

import fun.zhub.ppeng.dto.ManualCensorDTO;

import java.util.List;

/**
 * AdminCensorService interface
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
public interface AdminCensorService {

    /**
     * 获取菜谱列表
     *
     * @param timestamp 最后一篇菜谱的时间戳
     * @param pageSize 一页所呈现的信息数
     * @param key      Redis 的key
     * @return list
     */
    List<ManualCensorDTO> getReportedRecipeList(Long timestamp, Integer pageSize, String key);

    /**
     * 处理人工审核菜谱结果
     *
     * @param adminId  管理员id
     * @param recipeId 菜谱id
     * @param result   审核结果
     * @param reason   判定原因
     * @param isNotify 是否通知用户
     * @param key      redis的key前缀
     */
    void handlerRecipeCensorResult(Long adminId, Long recipeId, Integer result, String reason, Integer isNotify, String key);


}
