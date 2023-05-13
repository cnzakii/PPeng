package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.dto.AddUserMessageDTO;
import fun.zhub.ppeng.dto.MessageDTO;
import fun.zhub.ppeng.dto.MessageUpdateDTO;
import fun.zhub.ppeng.entity.Message;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.service.MessageService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 用户消息 接口
 *
 * @author Zaki
 * @since 2023-05-13
 */
@RestController
@RequestMapping("/user/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    /**
     * 添加用户消息，仅系统和管理员能添加
     *
     * @return success
     */
    @PostMapping("/add")
    public ResponseResult<String> addMeaasge(@RequestBody AddUserMessageDTO messageDTO) {
        Message message = BeanUtil.copyProperties(messageDTO, Message.class);

        messageService.saveMeaasge(message);

        return ResponseResult.success();
    }

    /**
     * 获取当前用户消息列表
     *
     * @param userId 用户id
     * @param page   页数
     * @param size   一页所呈现的信息数
     * @return list
     */
    @GetMapping("/list")
    public ResponseResult<List<MessageDTO>> listUserMessageList(@RequestParam("userId") Long userId, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "5") Integer size) {
        Long id = Long.valueOf((String) StpUtil.getLoginId());
        if (!Objects.equals(id, userId)) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "id错误");
        }

        var list = messageService.getMessagePage(userId, page, size)
                .stream()
                .map(bean -> BeanUtil.copyProperties(bean, MessageDTO.class))
                .toList();

        return ResponseResult.success(list);
    }

    /**
     * 改变用户消息状态--变成已读
     *
     * @param messageUpdateDTO 消息更新 数据传输对象
     * @return success
     */
    @PutMapping("/read")
    public ResponseResult<String> changeMessageStatus(@Valid @RequestBody MessageUpdateDTO messageUpdateDTO) {
        Long id = Long.valueOf((String) StpUtil.getLoginId());
        Long userId = messageUpdateDTO.getUserId();
        if (!Objects.equals(id, userId)) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "id错误");
        }
        Integer[] messageIds = messageUpdateDTO.getMessageIds();

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
    public ResponseResult<String> deleteMeaasge(@Valid @RequestBody MessageUpdateDTO messageUpdateDTO) {
        Long id = Long.valueOf((String) StpUtil.getLoginId());
        Long userId = messageUpdateDTO.getUserId();
        if (!Objects.equals(id, userId)) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "id错误");
        }
        Integer[] messageIds = messageUpdateDTO.getMessageIds();
        messageService.removeMessageById(userId, messageIds);
        return ResponseResult.success();
    }


}
