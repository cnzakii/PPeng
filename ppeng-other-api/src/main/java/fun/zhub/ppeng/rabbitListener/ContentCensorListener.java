package fun.zhub.ppeng.rabbitListener;

import cn.hutool.json.JSONUtil;
import com.zhub.ppeng.dto.TextContentCensorDTO;
import fun.zhub.ppeng.service.ContentCensorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.zhub.ppeng.constant.RabbitConstants.*;

/**
 * <p>
 * 内容审核监听队列
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-14
 **/
@Component
@Slf4j
public class ContentCensorListener {

    @Resource
    private ContentCensorService censorService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = TEXT_CONTENT_CENSOR_QUEUE),
            exchange = @Exchange(name = PPENG_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = ROUTING_TEXT_CENSOR
    ))
    public void ListenTextContentCensorQueue(String json) {
        TextContentCensorDTO censorDTO = JSONUtil.toBean(json, TextContentCensorDTO.class);

        String type = censorDTO.getType();
        Long id = censorDTO.getId();
        String content = censorDTO.getContent();

        switch (type) {
            case "nickName" -> censorService.censorNickName(id, content);
            case "recipe" -> censorService.censorRecipeText(id, content);
        }
    }

}
