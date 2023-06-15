package fun.zhub.ppeng.rabbitListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.zhub.ppeng.dto.ContentCensorDTO;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.service.ContentCensorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static fun.zhub.ppeng.common.ResponseStatus.HTTP_STATUS_500;
import static fun.zhub.ppeng.constant.RabbitConstants.*;

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


    /**
     * 监听内容审核队列
     *
     * @param json ContentCensorDTO
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = CONTENT_CENSOR_QUEUE),
            exchange = @Exchange(name = PPENG_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ROUTING_CONTENT_CENSOR
    ))
    public void listenTextContentCensorQueue(String json) {
        log.info("content.censor.queue队列监听到消息====》{}", json);

        ObjectMapper objectMapper = new ObjectMapper();
        ContentCensorDTO bean;
        try {
            bean = objectMapper.readValue(json, ContentCensorDTO.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException(HTTP_STATUS_500, "解析字符串出错");
        }


        String type = bean.getType();
        Long id = bean.getId();
        String[] data = bean.getData();

        switch (type) {
            case "nickName" -> censorService.censorNickName(id, data[0]);
            case "icon" -> censorService.censorUserIcon(id, data[0]);
            case "recipeImages" -> censorService.censorRecipeImages(id, data);
            case "recipeVideo" -> censorService.censorRecipeVideo(id, data);
        }
    }

}
