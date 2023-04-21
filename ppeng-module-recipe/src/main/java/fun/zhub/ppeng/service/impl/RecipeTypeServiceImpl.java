package fun.zhub.ppeng.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.entity.RecipeType;
import fun.zhub.ppeng.mapper.RecipeTypeMapper;
import fun.zhub.ppeng.service.RecipeTypeService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.zhub.ppeng.constant.RedisConstants.RECIPE_TYPE;

/**
 * <p>
 * 菜谱类型表 服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class RecipeTypeServiceImpl extends ServiceImpl<RecipeTypeMapper, RecipeType> implements RecipeTypeService {

    @Resource
    private RecipeTypeMapper recipeTypeMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 实现获取所有菜谱类型
     *
     * @return var
     */
    @Override
    @Cacheable(cacheNames = "recipeType",key ="#root.methodName" ,sync = true)
    public Map<String, List<String>> queryTotalRecipeTypeList() {
        // 先查询redis
        Map<Object, Object> objectMap = stringRedisTemplate.opsForHash().entries(RECIPE_TYPE);

        if (CollUtil.isNotEmpty(objectMap)) {
            return objectMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> (String) e.getKey(),
                            e -> Arrays.stream(((String) e.getValue()).split(","))
                                    .map(String::trim)
                                    .collect(Collectors.toList()),
                            (list1, list2) -> list1, HashMap::new));
        }


        // 查询父类
        List<RecipeType> parentTypesList = recipeTypeMapper.selectList(new LambdaQueryWrapper<RecipeType>().eq(RecipeType::getParentId, 0).orderByAsc(RecipeType::getScore));

        // 查询子类
        List<RecipeType> recipeTypesList = recipeTypeMapper.selectList(new LambdaQueryWrapper<RecipeType>().ne(RecipeType::getParentId, 0).orderByAsc(RecipeType::getScore));

        Map<String, List<String>> map = parentTypesList.stream()
                .collect(Collectors.toMap(
                        RecipeType::getName,
                        p -> recipeTypesList.stream()
                                .filter(c -> Objects.equals(c.getParentId(), p.getId()))
                                .map(RecipeType::getName)
                                .collect(Collectors.toList()),
                        (list1, list2) -> list1, HashMap::new));

        // 存入redis
        stringRedisTemplate.opsForHash().putAll(RECIPE_TYPE, map);


        return map;
    }
}
