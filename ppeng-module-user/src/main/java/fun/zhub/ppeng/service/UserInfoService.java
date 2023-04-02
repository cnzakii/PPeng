package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.DeleteUserInfoDTO;
import fun.zhub.ppeng.dto.UpdateUserInfoDTO;
import fun.zhub.ppeng.entity.UserInfo;

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
     * 根据用户id查询用户具体信息
     *
     * @param userId 用户id
     * @return UserInfo
     */
    UserInfo getUserInfoById(Long userId);

    /**
     * 根据用户id更新用户具体信息
     *
     * @param userInfo user
     * @return UserInfo
     */
    Boolean updataUserInfo(UpdateUserInfoDTO userInfo);
    /**
     * 根据用户id删除用户具体信息
     *
     * @param userInfo user
     * @return UserInfo
     */
    Boolean deleteUserInfo(DeleteUserInfoDTO userInfo);


}
