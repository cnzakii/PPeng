package fun.zhub.ppeng.service;

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
     * 审核菜谱内容
     *
     * @param recipeId id
     * @param text     菜谱内容
     */
    void censorRecipeText(Long recipeId, String text);


    /**
     * 审核头像
     *
     * @param id      用户id
     * @param content 内容
     */
    void censorUserIcon(Long id, String content);

    /*
     * TODO 审核菜谱图片，视频等
     */

}
