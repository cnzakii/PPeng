package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.dto.UserDTO;
import fun.zhub.ppeng.dto.login.PasswordLoginFormDTO;
import fun.zhub.ppeng.dto.login.VerifyCodeLoginFormDTO;
import fun.zhub.ppeng.dto.update.UpdateUserPasswordDTO;
import fun.zhub.ppeng.dto.update.UpdateUserPhoneDTO;
import fun.zhub.ppeng.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * UserService interface
 * 对用户的基本信息操作的接口类，包括：用户id，手机号，昵称，头像地址等
 * </p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-17
 */
public interface UserService extends IService<User> {


    /**
     * 通过验证码登录
     *
     * @param loginFormDTO 用户验证码登录结构体
     * @return user对象
     */
    User loginByVerifyCode(VerifyCodeLoginFormDTO loginFormDTO);


    /**
     * 通过密码登录
     *
     * @param loginFormDTO 用户密码登录结构体
     * @return User
     */
    User loginByPassword(PasswordLoginFormDTO loginFormDTO);


    /**
     * 在验证登录信息成功之后做的事情
     *
     * @param user 用户
     * @return authentication
     */
    String afterLogin(User user);

    /**
     * 根据id获取用户信息
     *
     * @param id id
     * @return userDTO
     */
    UserDTO getUserBaseInfoById(Long id);


    /**
     * 更新用户密码
     *
     * @param userPasswordDTO userPasswordDTO
     */
    void updatePassword(UpdateUserPasswordDTO userPasswordDTO);


    /**
     * 更新用户手机号
     *
     * @param userPhoneDTO userPhoneDTO
     */
    void updatePhone(UpdateUserPhoneDTO userPhoneDTO);


    /**
     * 更新用户昵称
     *
     * @param id       id
     * @param nickName 昵称
     */
    void updateNickNameById(Long id, String nickName);


    /**
     * 更新用户头像
     *
     * @param id   id
     * @param icon 头像
     * @return 头像存储路径
     */
    String updateIconById(Long id, MultipartFile icon);

    /**
     * 根据id删除用户
     *
     * @param id id
     */
    void deleteUserById(Long id);

    /**
     * 根据id和手机号创建新用户
     *
     * @param id    id
     * @param phone 用户手机号
     * @return User对象
     */
    User createUser(Long id, String phone);


    /**
     * 匹配 验证码和手机号
     *
     * @param phone phone
     * @param code  验证码
     * @param key   redis存储对应验证码的前缀
     * @return 是否匹配
     */
    Boolean verifyPhone(String phone, String code, String key);


}
