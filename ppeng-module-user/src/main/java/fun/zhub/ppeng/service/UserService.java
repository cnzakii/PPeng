package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.dto.PasswordLoginFormDTO;
import fun.zhub.ppeng.dto.VerifyCodeLoginFormDTO;
import fun.zhub.ppeng.entity.User;

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
     * 根据id和手机号创建新用户
     *
     * @param id    id
     * @param phone 用户手机号
     * @return User对象
     */
    User createUser(Long id, String phone);
}
