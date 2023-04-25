package fun.zhub.ppeng.rabbitListener;

import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.dto.ContentCensorDTO;
import fun.zhub.ppeng.service.ContentCensorService;
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

        ContentCensorDTO bean = JSONUtil.toBean(json, ContentCensorDTO.class);

        log.debug("content.censor.queue队列监听到消息====》{}", bean);

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
