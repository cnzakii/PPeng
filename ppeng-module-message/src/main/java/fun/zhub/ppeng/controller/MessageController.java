package fun.zhub.ppeng.controller;

import cn.hutool.core.bean.BeanUtil;
import fun.zhub.ppeng.common.PageBean;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.dto.AddUserMessageDTO;
import fun.zhub.ppeng.dto.MessageDTO;
import fun.zhub.ppeng.dto.MessageUpdateDTO;
import fun.zhub.ppeng.entity.Message;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.service.MessageService;
import fun.zhub.ppeng.validation.annotation.MatchToken;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/**
 * 用户消息 接口
 *
 * @author Zaki
 * @since 2023-05-13
 */
@RestController
@RequestMapping("/message")
@Validated
public class MessageController {

    @Resource
    private MessageService messageService;

    /**
     * 添加用户消息，仅系统和管理员能添加
     *
     * @return true or false
     */
    @PostMapping("/add")
    public Boolean addMeaasge(@RequestBody AddUserMessageDTO messageDTO) {
        Message message = BeanUtil.copyProperties(messageDTO, Message.class);
        return messageService.save(message);
    }

    /**
     * 获取当前用户消息列表
     *
     * @param userId    用户id
     * @param timestamp 时间戳
     * @param size      一页所呈现的信息数
     * @return list
     */
    @GetMapping("/list")
    public ResponseResult<PageBean<MessageDTO>> listUserMessageList(@RequestParam("userId") @MatchToken Long userId, @RequestParam(value = "timestamp", defaultValue = "") Long timestamp, @RequestParam(value = "size", defaultValue = "5") Integer size) {
        timestamp = Optional.ofNullable(timestamp).orElseGet(System::currentTimeMillis);

        var pageBean = messageService.getMessagePage(userId, timestamp, size);

        return ResponseResult.success(pageBean);
    }

    /**
     * 改变用户消息状态--变成已读
     *
     * @param messageUpdateDTO 消息更新 数据传输对象
     * @return success
     */
    @PutMapping("/read")
    public ResponseResult<String> changeMessageStatus(@Validated @RequestBody MessageUpdateDTO messageUpdateDTO) {
        Long userId = messageUpdateDTO.getUserId();

        Integer[] messageIds = messageUpdateDTO.getMessageIds();
        // 校验messageIds
        if (messageIds.length == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "messageIds不能为null");
        }

        messageService.changeMessageStatusById(userId, 1, messageIds);

        return ResponseResult.success();
    }

    /**
     * 删除用户消息
     *
     * @param messageUpdateDTO 消息更新 数据传输对象
     * @return success
     */
    @DeleteMapping("/delete")
    public ResponseResult<String> deleteMeaasge(@Validated @RequestBody MessageUpdateDTO messageUpdateDTO) {
        Long userId = messageUpdateDTO.getUserId();

        Integer[] messageIds = messageUpdateDTO.getMessageIds();
        // 校验messageIds
        if (messageIds.length == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "messageIds不能为null");
        }


        messageService.removeMessageById(userId, messageIds);
        return ResponseResult.success();
    }


}
