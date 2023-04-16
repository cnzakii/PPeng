package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.dto.UserInfoDTO;
import fun.zhub.ppeng.dto.login.LoginFormDTO;
import fun.zhub.ppeng.dto.register.RegisterDTO;
import fun.zhub.ppeng.dto.update.UpdateUserEmailDTO;
import fun.zhub.ppeng.dto.update.UpdateUserPasswordDTO;
import fun.zhub.ppeng.entity.User;

import java.util.Map;

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


    void register(RegisterDTO registerDTO);


    /**
     * 通过密码登录
     *
     * @param loginFormDTO 用户密码登录结构体
     * @return user
     */
    User loginByPassword(LoginFormDTO loginFormDTO);


    /**
     * 微信登录
     *
     * @param code 临时凭证
     * @return user 用户 ， Boolean 是否登录
     */
    Map<String, Object> loginByWeChat(String code);


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
     * @return UserInfoDTO
     */
    UserInfoDTO getUserInfoById(Long id);


    /**
     * 更新用户密码
     *
     * @param userPasswordDTO userPasswordDTO
     */
    void updatePassword(UpdateUserPasswordDTO userPasswordDTO);


    /**
     * 更新用户邮箱
     *
     * @param userEmailDTO 邮箱
     */
    void updateEmail(UpdateUserEmailDTO userEmailDTO);


    /**
     * 更新用户的昵称和头像
     *
     * @param userId   userId
     * @param nickName 昵称
     * @param icon     头像url
     */
    void updateNickNameAndIcon(Long userId, String nickName, String icon);


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
     * @return UserInfoDTO
     */
    UserInfoDTO createUser(User user);


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
