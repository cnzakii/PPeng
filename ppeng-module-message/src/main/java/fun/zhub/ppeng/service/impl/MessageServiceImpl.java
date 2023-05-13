package fun.zhub.ppeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.entity.Message;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.mapper.MessageMapper;
import fun.zhub.ppeng.service.MessageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MessageService 实现类
 *
 * @author Zaki
 * @since 2023-05-13
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    /**
     * 保存消息
     *
     * @param message 消息类
     */
    @Override
    public void saveMeaasge(Message message) {
        message.setStatus(0);
        message.setIsDeleted(0);
        message.setCreateTime(LocalDateTime.now());

        int i = messageMapper.insert(message);
        if (i == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "添加消息失败");
        }
    }

    /**
     * 查询用户消息
     *
     * @param userId   用户id
     * @param pageNum  页数
     * @param pageSize 一页所呈现的页数
     * @return list
     */
    @Override
    public List<Message> getMessagePage(Long userId, Integer pageNum, Integer pageSize) {
        Page<Message> page = new Page<>(pageNum, pageSize);
        Page<Message> messagePage = messageMapper.selectPage(page, new LambdaQueryWrapper<Message>()
                .eq(Message::getUserId, userId)
                .orderByAsc(Message::getStatus)
                .orderByDesc(Message::getCreateTime));

        return messagePage.getRecords();
    }

    /**
     * 改变用户消息状态
     *
     * @param userId     用户id
     * @param messageIds 消息id
     * @param status     消息状态
     */
    @Override
    public void changeMessageStatusById(Long userId, Integer status, Integer... messageIds) {
        // 检查参数
        if (status != 0 && status != 1) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "status参数错误");
        }

        int i = messageMapper.update(null, new LambdaUpdateWrapper<Message>()
                .set(Message::getStatus, status)
                .eq(Message::getUserId, userId)
                .in(Message::getId, (Object[]) messageIds));
        if (i == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "删除消息失败");
        }

        if (i != messageIds.length) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "部分消息不属于该用户");
        }

    }


    /**
     * 删除用户消息
     *
     * @param userId     用户id
     * @param messageIds 消息id
     */
    @Override
    public void removeMessageById(Long userId, Integer... messageIds) {
        int i = messageMapper.delete(new LambdaQueryWrapper<Message>().eq(Message::getUserId, userId).in(Message::getId, (Object[]) messageIds));

        if (i == 0) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "删除消息失败");
        }

        if (i != messageIds.length) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "部分消息不属于该用户");
        }

    }


}
