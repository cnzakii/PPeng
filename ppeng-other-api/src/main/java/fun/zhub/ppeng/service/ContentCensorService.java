package fun.zhub.ppeng.service;

import org.json.JSONObject;

/**
 * <p>
 * 内容审核interface
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-14
 **/
public interface ContentCensorService {

    /**
     * 审查用户昵称
     *
     * @param userId   id
     * @param nickName 昵称
     */
    void censorNickName(Long userId, String nickName);


    /**
     * 审核头像
     *
     * @param userId 用户id
     * @param path   头像路径
     */
    void censorUserIcon(Long userId, String path);

    /**
     * 审核菜谱图文
     *
     * @param recipeId   菜谱id
     * @param data 数据
     */
    void censorRecipeImages(Long recipeId, String[] data);

    /**
     * 审核菜谱视频
     *
     * @param recipeId 菜谱id
     * @param data     数据
     */
    void censorRecipeVideo(Long recipeId, String[] data);


    /**
     * 通用 response 请求处理
     *
     * @param response response
     * @return 不合规信息，如果合规，返回null
     */
    String getMsgFromResponse(JSONObject response);


}
