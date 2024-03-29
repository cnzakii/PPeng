package fun.zhub.ppeng.canalHandler;

import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.canal.AbstractCanalHandler;
import fun.zhub.ppeng.canal.CanalTable;
import fun.zhub.ppeng.entity.Recipe;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

import static fun.zhub.ppeng.constant.RabbitConstants.*;
import static fun.zhub.ppeng.constant.RedisConstants.RECIPE_RECOMMEND_COMMON_KEY;
import static fun.zhub.ppeng.constant.RedisConstants.RECIPE_RECOMMEND_PROFESSIONAL_KEY;

/**
 * recipe表中数据发生变化 的处理类
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-08
 **/
@CanalTable("t_recipe")
@Component
@Slf4j
public class RecipeCanalHandler extends AbstractCanalHandler<Recipe> {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;


    /**
     * 处理新增的数据
     *
     * @param data 插入的数据
     */
    @Override
    public void insert(Recipe data) {
        // 转换成时间戳
        long timestamp = data.getCreateTime().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        // 查看新增的菜谱是否专业
        String key = (data.getIsProfessional() == 1) ? RECIPE_RECOMMEND_PROFESSIONAL_KEY : RECIPE_RECOMMEND_COMMON_KEY;

        // 存入redis的zset集合
        stringRedisTemplate.opsForZSet().add(key, JSONUtil.toJsonStr(data), timestamp);
    }

    /**
     * 处理更新的数据
     *
     * @param oldData 旧数据
     * @param newData 新数据
     */
    @Override
    public void update(Recipe oldData, Recipe newData) {
        // 查看新增的菜谱是否专业
        String key = (oldData.getIsProfessional() == 1) ? RECIPE_RECOMMEND_PROFESSIONAL_KEY : RECIPE_RECOMMEND_COMMON_KEY;

        // 查看是否违规
        if (newData.getIsBaned() == 1) {
            // 如果违规
            // 从推荐列表中删除
            stringRedisTemplate.opsForZSet().remove(key, JSONUtil.toJsonStr(oldData));

            // 如果是普通菜谱，则通过MQ删除ElasticSearch中的相关数据
            if (oldData.getIsProfessional() == 0) {
                rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_RECIPE_DELETE, oldData.getId());
            }

            return;
        }

        // 转换成时间戳
        long timestamp = oldData.getUpdateTime().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();


        // 删除redis中的缓存
        stringRedisTemplate.opsForZSet().remove(key, JSONUtil.toJsonStr(oldData));

        // 添加新的缓存
        stringRedisTemplate.opsForZSet().add(key, JSONUtil.toJsonStr(newData), timestamp);

        // 如果是普通菜谱，则通过MQ更新ElasticSearch中的相关数据
        if (oldData.getIsProfessional() == 0) {
            log.info("传入消息队列，写入ElasticSearch");
            rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_RECIPE_ADD, JSONUtil.toJsonStr(newData));
        }
    }

    /**
     * 处理删除的数据
     *
     * @param data 删除操作
     */
    @Override
    public void delete(Recipe data) {

        // 查看新增的菜谱是否专业
        String key = (data.getIsProfessional() == 1) ? RECIPE_RECOMMEND_PROFESSIONAL_KEY : RECIPE_RECOMMEND_COMMON_KEY;

        // 从推荐列表中删除
        stringRedisTemplate.opsForZSet().remove(key, JSONUtil.toJsonStr(data));

        // 如果是普通菜谱，则通过MQ删除ElasticSearch中的相关数据
        if (data.getIsProfessional() == 0) {
            rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_RECIPE_DELETE, data.getId());
        }
    }
}
