package fun.zhub.ppeng.controller.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.dto.AddUserMessageDTO;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.exception.GlobalBlockHandler;
import fun.zhub.ppeng.feign.MessageService;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Resource
    private MessageService messageService;


    @PostMapping("/nick/name")
    @SentinelResource(value = "handleBadNickName", blockHandlerClass = GlobalBlockHandler.class, blockHandler = "handleCommonBlockException")
    public ResponseResult<String> handleBadNickName(@RequestParam("id") Long id, @RequestParam("msg") String msg) {
        String newNickName = BAD_NICK_NAME_PREFIX + RandomUtil.randomString(10).toUpperCase();

        // 根据id获取用户
        User user = userService.getUserInfoById(id);
        if (BeanUtil.isEmpty(user)) {
            log.error("用户({})不存在，更新违规昵称失败", id);
            return ResponseResult.fail("用户不存在");
        }
        user.setNickName(newNickName);

        // 更新用户昵称
        userService.updateUserInfo(user);


        // 调用信息模块，告知用户违规原因
        String title = "昵称违规通知";
        String content = "尊敬的用户，我们注意到您的昵称存在违规内容，涉嫌：" + msg + "，请您尽快修改昵称，以符合我们的社区准则和规定。如有任何疑问，请联系我们的客服团队。谢谢合作！";
        AddUserMessageDTO addUserMessageDTO = new AddUserMessageDTO(id, title, content);
        Boolean a = messageService.addMeaasge(addUserMessageDTO);

        if (BooleanUtil.isFalse(a)) {
            log.error("发送({})违规通知失败", id);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "发送违规通知失败");
        }

        return ResponseResult.success();
    }


}
