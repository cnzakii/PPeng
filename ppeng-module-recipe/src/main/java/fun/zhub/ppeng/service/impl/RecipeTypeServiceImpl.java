package fun.zhub.ppeng.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
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

import static fun.zhub.ppeng.constant.RedisConstants.RECIPE_TYPE_DETAIL_KEY;
import static fun.zhub.ppeng.constant.RedisConstants.RECIPE_TYPE_NAME_KEY;

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
     * @return map
     */
    @Override
    @Cacheable(cacheNames = "recipeType", key = "#root.methodName", sync = true)
    public Map<String, List<String>> queryTotalNameList() {
        // 先查询redis
        Map<Object, Object> objectMap = stringRedisTemplate.opsForHash().entries(RECIPE_TYPE_NAME_KEY);

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
        stringRedisTemplate.opsForHash().putAll(RECIPE_TYPE_NAME_KEY, map);


        return map;
    }

    /**
     * 实现获取所有菜谱对象集合
     *
     * @return list
     */
    @Override
    @Cacheable(cacheNames = "recipeType", key = "#root.methodName", sync = true)
    public List<RecipeType> queryTotalRecipeTypeList() {
        // 先从redis中查询
        List<String> list = stringRedisTemplate.opsForList().range(RECIPE_TYPE_DETAIL_KEY, 0, -1);

        // 如果有则返回
        if (list != null && !list.isEmpty()) {
            return list.stream()
                    .map(s -> JSONUtil.toBean(s, RecipeType.class))
                    .collect(Collectors.toList());
        }


        // 如果没有，则查询数据库
        List<RecipeType> recipeTypeList = recipeTypeMapper.selectList(null);

        String[] recipeTypeArray = recipeTypeList.stream()
                .map(JSONUtil::toJsonStr)
                .toArray(String[]::new);

        stringRedisTemplate.opsForList().leftPushAll(RECIPE_TYPE_DETAIL_KEY, recipeTypeArray);

        return recipeTypeList;
    }



    /**
     * 实现根据id获取名字
     *
     * @param recipeTypeId recipeTypeId
     * @return name
     */
    @Override
    public String getNameById(Integer recipeTypeId) {
        // 获取所有菜谱类型对象的集合
        List<RecipeType> list = queryTotalRecipeTypeList();

        return list.stream()
                .filter(recipeType -> Objects.nonNull(recipeType) && Objects.equals(recipeType.getId(), recipeTypeId))
                .map(RecipeType::getName)
                .findFirst()
                .orElse(null);
    }

}
