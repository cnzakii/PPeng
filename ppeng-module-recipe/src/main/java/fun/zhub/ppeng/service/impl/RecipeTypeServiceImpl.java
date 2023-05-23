package fun.zhub.ppeng.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.dto.RecipeTypeDTO;
import fun.zhub.ppeng.entity.RecipeType;
import fun.zhub.ppeng.mapper.RecipeTypeMapper;
import fun.zhub.ppeng.service.RecipeTypeService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static fun.zhub.ppeng.constant.RedisConstants.*;

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
     * 实现获取所有菜谱对象集合
     *
     * @return list
     */
    @Override
    @Cacheable(cacheNames = "recipeType", key = "#root.methodName", sync = true)
    public List<RecipeType> queryTotalRecipeTypeList() {
        // 先从redis中查询
        List<String> list = stringRedisTemplate.opsForList().range(RECIPE_TOTAL_TYPE_KEY, 0, -1);

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

        stringRedisTemplate.opsForList().leftPushAll(RECIPE_TOTAL_TYPE_KEY, recipeTypeArray);

        return recipeTypeList;
    }


    /**
     * 实现根据id获取名字
     *
     * @param recipeTypeId recipeTypeId
     * @return name
     */
    @Override
    @Cacheable(cacheNames = "recipeTypeName", key = "#recipeTypeId", sync = true)
    public String getNameById(Integer recipeTypeId) {
        // 获取所有菜谱类型对象的集合
        List<RecipeType> list = queryTotalRecipeTypeList();

        return list.stream()
                .filter(recipeType -> Objects.nonNull(recipeType) && Objects.equals(recipeType.getId(), recipeTypeId))
                .map(RecipeType::getName)
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取所有的父类集合
     *
     * @return list集合
     */
    @Override
    @Cacheable(cacheNames = "parentRecipeTypeList", key = "#root.methodName", sync = true)
    public List<RecipeTypeDTO> getParentRecipeTypeList() {
        // 先从redis中查询
        List<String> list = stringRedisTemplate.opsForList().range(RECIPE_PARENT_TYPE_KEY, 0, -1);

        // 如果有则返回
        if (list != null && !list.isEmpty()) {
            return list.stream()
                    .map(s -> JSONUtil.toBean(s, RecipeTypeDTO.class))
                    .collect(Collectors.toList());
        }


        // 如果没有，则查询数据库
        List<RecipeType> recipeTypeList = recipeTypeMapper.selectList(new LambdaQueryWrapper<RecipeType>().eq(RecipeType::getParentId, 0).orderByAsc(RecipeType::getScore));

        if (CollUtil.isEmpty(recipeTypeList)) {
            return null;
        }

        // 将RecipeType转成RecipeDTO
        List<RecipeTypeDTO> typeDTOList = recipeTypeList.stream().map(bean -> BeanUtil.copyProperties(bean, RecipeTypeDTO.class)).toList();

        // 写入redis
        String[] recipeTypeArray = typeDTOList.stream()
                .map(JSONUtil::toJsonStr)
                .toArray(String[]::new);
        stringRedisTemplate.opsForList().leftPushAll(RECIPE_PARENT_TYPE_KEY, recipeTypeArray);

        return typeDTOList;
    }

    /**
     * 根据父类id获取 子类集合
     *
     * @return list集合
     */
    @Override
    @Cacheable(cacheNames = "recipeTypeList", key = "#parentId", sync = true)
    public List<RecipeTypeDTO> getSubRecipeTypeListByParentId(Integer parentId) {
        String key = RECIPE_SUB_TYPE_KEY + parentId;
        // 先从redis中查询
        List<String> list = stringRedisTemplate.opsForList().range(key, 0, -1);

        // 如果有则返回
        if (list != null && !list.isEmpty()) {
            return list.stream()
                    .map(s -> JSONUtil.toBean(s, RecipeTypeDTO.class))
                    .collect(Collectors.toList());
        }


        // 如果没有，则查询数据库
        List<RecipeType> recipeTypeList = recipeTypeMapper.selectList(new LambdaQueryWrapper<RecipeType>()
                .eq(RecipeType::getParentId, parentId)
                .orderByAsc(RecipeType::getScore));

        if (CollUtil.isEmpty(recipeTypeList)) {
            return null;
        }

        // 将RecipeType转成RecipeDTO
        List<RecipeTypeDTO> typeDTOList = recipeTypeList.stream().map(bean -> BeanUtil.copyProperties(bean, RecipeTypeDTO.class)).toList();

        // 写入redis
        String[] recipeTypeArray = typeDTOList.stream()
                .map(JSONUtil::toJsonStr)
                .toArray(String[]::new);
        stringRedisTemplate.opsForList().leftPushAll(key, recipeTypeArray);

        return typeDTOList;
    }

}
