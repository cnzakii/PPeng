package fun.zhub.ppeng.constant;

/**
 * <p>
 * 和Redis相关的常量，包括但不限于Key的前缀，过期时间
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-18
 **/
public interface RedisConstants {

// -----------------------------------------------------------------------------------邮件相关
    /**
     * 验证码注册前缀
     */
    String REGISTER_CODE_KEY = "email:register:code:";

    /**
     * 验证码更新邮箱前缀
     */
    String UPDATE_EMAIL_CODE_KEY = "email:update:email:code:";

    /**
     * 验证码更新密码前缀
     */
    String UPDATE_PASSWORD_CODE_KEY = "email:update:password:code:";


    /**
     * 验证码删除用户前缀
     */
    String DELETE_USER_CODE_KEY = "email:delete:user:code:";


    /**
     * 邮箱验证码过期时间
     */
    Long EMAIL_CODE_TTL = 2L;

// -----------------------------------------------------------------------------------用户相关
    /**
     * 用户角色前缀
     */
    String USER_ROLE = "user:role:";


    /**
     * 用户信息
     */
    String USER_INFO = "user:info:";

    /**
     * 用户基本信息过期时间
     */
    Long USER_INFO_TTL = 12L;


    /**
     * 用户角色信息过期时间
     */

    Long ROLE_USER_TTL = 12L;


    /**
     * 用户关注数据前缀
     */
    String USER_FOLLOWS_KEY = "user:follows:";

    /**
     * 用户关注数据过期时间
     */
    Long USER_FOLLOWS_TTL = 30L;


    /**
     * 用户粉丝数据前缀
     */
    String USER_FANS_KEY = "user:fans:";

    /**
     * 用户粉丝数据过期时间
     */
    Long USER_FANS_TTL = 30L;

    /**
     * 用户点赞菜谱数据前缀
     */
    String USER_LIKE_KEY = "user:like:";

    /**
     * 用户点赞菜谱数据过期时间
     */
    Long USER_LIKE_TTL = 30L;

    /**
     * 用户收藏菜谱数据前缀
     */
    String USER_COLLECT_KEY = "user:collect:";

    /**
     * 用户收藏菜谱数据过期时间
     */
    Long USER_COLLECT_TTL = 30L;

// -----------------------------------------------------------------------------------菜谱相关

    /**
     * 菜谱所有类型集合
     */
    String RECIPE_TOTAL_TYPE_KEY = "recipe:type:total:list";

    /**
     * 菜谱父类集合
     */
    String RECIPE_PARENT_TYPE_KEY = "recipe:type:parent:list";

    /**
     * 菜谱子类集合前缀
     */
    String RECIPE_SUB_TYPE_KEY = "recipe:type:sub:list:";

    /**
     * 普通菜谱推荐前缀
     */
    String RECIPE_RECOMMEND_COMMON_KEY = "recipe:recommend:common";

    /**
     * 专业菜谱推荐前缀
     */
    String RECIPE_RECOMMEND_PROFESSIONAL_KEY = "recipe:recommend:professional";

    /**
     * 菜谱举报前缀
     */
    String REPORT_RECIPE_KEY = "report:recipe";

    /**
     * 菜谱上述前缀
     */
    String APPEAL_RECIPE_KEY = "appeal:recipe";

    /**
     * 菜谱id映射前缀
     */
    String MAP_RECIPE_ID_KEY = "map:recipe:id:";

    /**
     * 菜谱id映射过期时间
     */
    Long MAP_RECIPE_ID_TTL = 7L;

// -----------------------------------------------------------------------------------管理员相关

    /**
     * 管理员信息过期前缀
     */
    String ADMIN_INFO = "admin:info:";

    /**
     * 管理员信息过期时间
     */
    Long ADMIN_INFO_TTL = 12L;
}
