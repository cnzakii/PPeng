package fun.zhub.ppeng.controller;

import cn.hutool.core.util.RandomUtil;
import com.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import static com.zhub.ppeng.constant.RoleConstants.BAD_NICK_NAME_PREFIX;

/**
 * <p>
 * 用户审查违规之后的处理
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-14
 **/
@RestController
@RequestMapping("/handle/user")
public class UserBadContentController {

    @Resource
    private UserService userService;


    @PostMapping("/nick/name/{id}")
    public ResponseResult<String> handleBadNickName(@PathVariable("id")Long id) {
        String newNickName = BAD_NICK_NAME_PREFIX + RandomUtil.randomString(10);
        userService.updateNickNameById(id, newNickName);

        return ResponseResult.success();
    }



}
