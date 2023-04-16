package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.entity.UserInfo;

import java.time.LocalDate;

/**
 * <p>
 * UserInfoService interface
 * 更加详细的用户信息：所在城市，自我介绍，性别，生日 等 服务类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
public interface UserInfoService extends IService<UserInfo> {



    /**
     * 更新除了粉丝数，关注数以外的信息
     *
     * @param id        用户Id
     * @param address   地址
     * @param introduce 自我介绍
     * @param gender    性别
     * @param birthday  生日
     */
    void updateUserInfo(Long id, String address, String introduce, Byte gender, LocalDate birthday);

    /**
     * 更新粉丝或者关注数
     *
     * @param name   字段名
     * @param userId id
     * @param type   类型
     */
    void updateFollowOrFans(String name, Long userId, String type);

    /**
     * 根据用户id删除用户具体信息
     *
     * @param id id
     */
    void deleteUserInfoById(Long id);


    /**
     * 根据id创建用户详细信息
     *
     * @param id id
     * @return userinfo
     */
    UserInfo createUserInfoById(Long id);


}
