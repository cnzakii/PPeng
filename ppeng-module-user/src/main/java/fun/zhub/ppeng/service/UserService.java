package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * UserService interface
 * 对用户的基本信息操作的接口类，包括：用户id，email，昵称，头像地址等
 * </p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-17
 */
public interface UserService extends IService<User> {


    /**
     * 邮箱注册
     *
     * @param email    邮箱
     * @param password 密码(明文)
     */
    void register(String email, String password);


    /**
     * 通过密码登录
     *
     * @param email    邮件
     * @param password 密码
     * @return user
     */
    User loginByPassword(String email, String password);


    /**
     * 微信登录
     *
     * @param code 临时凭证
     * @return user 用户 ， Boolean 是否登录
     */
    User loginByWeChat(String code);


    /**
     * 在验证登录信息成功之后做的事情
     *
     * @param user user
     * @return authentication
     */
    String afterLogin(User user);


    /**
     * 根据id获取用户信息
     *
     * @param id id
     * @return User
     */
    User getUserInfoById(Long id);


    /**
     * 更新用户密码
     *
     * @param userId      用户id
     * @param newPassword 新密码(明文)
     */
    void updatePassword(Long userId, String newPassword);


    /**
     * 更新用户邮箱
     *
     * @param userId   userId
     * @param newEmail 邮箱
     */
    void updateEmail(Long userId, String newEmail);


    /**
     * 更新用户信息
     *
     * @param user User
     */
    void updateUserInfo(User user);

    /**
     * 更新用户头像
     *
     * @param userId 用户id
     * @param icon   头像
     * @return 头像地址
     */
    String updateUserIcon(Long userId, MultipartFile icon);

    /**
     * 根据用户ID来更新用户的粉丝数、关注数
     *
     * @param field  字段名
     * @param userId id
     * @param change 变化的数值
     */
    void updateUserStatsById(String field, Long userId, int change);


    /**
     * 根据id删除用户
     *
     * @param id id
     */
    void deleteUserById(Long id);


    /**
     * 根据user创建新用户
     *
     * @param user user
     * @return User
     */
    User createUser(User user);


    /**
     * 匹配 验证码和email
     *
     * @param email email
     * @param code  验证码
     * @param key   redis存储对应验证码的前缀
     * @return 是否匹配
     */
    Boolean verifyEmail(String email, String code, String key);


}
