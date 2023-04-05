package fun.zhub.ppeng.controller;

import com.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.DeleteUserInfoDTO;
import fun.zhub.ppeng.dto.UpdateUserInfoDTO;
import fun.zhub.ppeng.service.UserInfoService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户信息表，包含更加详细的用户信息：所在城市，自我介绍，性别，生日 等 前端控制器
 * </p>
 *
 * @author lbl
 * @since 2023-03-17
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;




    /**
     * 更新用户具体信息
     *
     * @param userInfo userInfo
     * @return success
     */
    @PutMapping("/current")
    public ResponseResult<String> updateUserInfo(@Valid UpdateUserInfoDTO userInfo) {
        userInfoService.updateUserInfo(userInfo);
        return ResponseResult.success();
    }

    /**
     * 删除用户具体信息
     *
     * @param userInfo userInfo
     * @return success
     */
    @DeleteMapping("/current")
    public ResponseResult<String> deleteUserInfo(@RequestBody @Valid DeleteUserInfoDTO userInfo) {
        userInfoService.deleteUserInfo(userInfo);
        return ResponseResult.success();
    }
}
