package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.entity.Message;

import java.util.List;

/**
 * MessageService interface
 *
 * @author Zaki
 * @since 2023-05-13
 */
public interface MessageService extends IService<Message> {



    /**
     * 查询用户消息
     * @param userId 用户id
     * @param pageNum 页数
     * @param pageSize 一页所呈现的页数
     * @return list
     */
    List<Message> getMessagePage(Long userId, Integer pageNum, Integer pageSize);


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
