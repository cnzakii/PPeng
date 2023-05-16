package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.common.PageBean;
import fun.zhub.ppeng.dto.MessageDTO;
import fun.zhub.ppeng.entity.Message;

/**
 * MessageService interface
 *
 * @author Zaki
 * @since 2023-05-13
 */
public interface MessageService extends IService<Message> {


    /**
     * 查询用户消息
     *
     * @param userId    用户id
     * @param timestamp 时间戳
     * @param pageSize  一页所呈现的页数
     * @return pageBean
     */
    PageBean<MessageDTO> getMessagePage(Long userId, Long timestamp, Integer pageSize);


    /**
     * @param userId     用户id
     * @param messageIds 消息id
     * @param status     消息状态
     */
    void changeMessageStatusById(Long userId, Integer status, Integer... messageIds);


    /**
     * 删除用户消息
     *
     * @param userId     用户id
     * @param messageIds 消息id
     */
    void removeMessageById(Long userId, Integer... messageIds);


}
