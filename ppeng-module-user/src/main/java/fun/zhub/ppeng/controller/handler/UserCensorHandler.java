package fun.zhub.ppeng.controller.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.exception.GlobalBlockHandler;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserCensorHandler {

    @Resource
    private UserService userService;


    @PostMapping("/nick/name/{id}")
    @SentinelResource(value = "handleBadNickName", blockHandlerClass = GlobalBlockHandler.class, blockHandler = "handleCommonBlockException")
    public ResponseResult<String> handleBadNickName(@PathVariable("id") Long id) {
        String newNickName = BAD_NICK_NAME_PREFIX + RandomUtil.randomString(10).toUpperCase();

        User user = userService.getUserInfoById(id);

        if (BeanUtil.isEmpty(user)) {
            log.error("用户({})不存在，更新违规昵称失败", id);
            return ResponseResult.fail("用户不存在");
        }
        user.setNickName(newNickName);
        userService.updateUserInfo(user);
        return ResponseResult.success();
    }


}
