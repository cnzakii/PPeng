package fun.zhub.ppeng.rabbitListener;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.entity.RecipeVO;
import fun.zhub.ppeng.service.RecipeSearchService;
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
 * 监听RabbitMQ队列，添加菜谱内容进ElasticSearch，或者从中删除
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-21
 **/
@Component
@Slf4j
public class RecipeListener {


    @Resource
    private RecipeSearchService recipeSearchService;

    /**
     * 监听菜谱添加队列，将菜谱添加进ElasticSearch
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RECIPE_ADD_QUEUE),
            exchange = @Exchange(name = PPENG_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ROUTING_RECIPE_ADD
    ))
    public void listenUserCacheQueue(String json) {
        Recipe bean = JSONUtil.toBean(json, Recipe.class);
        log.info("开始添加菜谱数据====》{}", bean);

        RecipeVO recipeVO = BeanUtil.copyProperties(bean, RecipeVO.class);

        recipeSearchService.saveRecipeVO(recipeVO);
    }


    /**
     * 监听菜谱删除队列
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RECIPE_DELETE_QUEUE),
            exchange = @Exchange(name = PPENG_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ROUTING_RECIPE_DELETE
    ))
    public void listenUserCacheDeleteQueue(Long id) {
        log.info("开始删除菜谱({})数据", id);
        recipeSearchService.removeRecipeVoById(id);
    }


}
