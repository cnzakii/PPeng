package fun.zhub.ppeng.rabbitListener;

import fun.zhub.ppeng.service.FileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static fun.zhub.ppeng.constant.RabbitConstants.*;

/**
 * <p>
 * 文件处理监听队列
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-17
 **/

@Component
@Slf4j
public class FileListener {

    @Resource
    private FileService fileService;


    /**
     * 监听文件删除队列 file.delete.queue
     *
     * @param filePath 需要删除的文件的路径(不包含根路径)
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = FILE_DELETE_QUEUE),
            exchange = @Exchange(name = PPENG_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ROUTING_FILE_DELETE
    ))
    public void deleteFile(String filePath) {
        log.info("file.delete.queue队列监听到消息====》{}", filePath);
        fileService.deleteFile(filePath);
    }


}
