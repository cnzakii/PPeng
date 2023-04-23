package fun.zhub.ppeng.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.exception.GlobalBlockHandler;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static fun.zhub.ppeng.constant.RoleConstants.BAD_NICK_NAME_PREFIX;

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
public class UserBadContentHandler {

    @Resource
    private UserService userService;


    @PostMapping("/nick/name/{id}")
    @SentinelResource(value = "handleBadNickName", blockHandlerClass = GlobalBlockHandler.class, blockHandler = "handleCommonBlockException")
    public ResponseResult<String> handleBadNickName(@PathVariable("id") Long id) {
        String newNickName = BAD_NICK_NAME_PREFIX + RandomUtil.randomString(10);
        userService.updateUserInfo(id, newNickName, null, null, null, null, null);

        return ResponseResult.success();
    }


}
