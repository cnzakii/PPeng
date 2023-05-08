package fun.zhub.ppeng.canalHandler;

import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.canal.AbstractCanalHandler;
import fun.zhub.ppeng.canal.CanalTable;
import fun.zhub.ppeng.entity.Recipe;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

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

        // 转换成时间戳
        long timestamp = oldData.getUpdateTime().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();

        // 删除redis中的缓存
        stringRedisTemplate.opsForZSet().remove(key, JSONUtil.toJsonStr(oldData));

        // 添加新的缓存
        stringRedisTemplate.opsForZSet().remove(key, JSONUtil.toJsonStr(newData), timestamp);

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

        // 删除redis中的缓存
        stringRedisTemplate.opsForZSet().remove(key, JSONUtil.toJsonStr(data));
    }
}
