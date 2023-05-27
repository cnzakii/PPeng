package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.common.PageBean;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.ContentCensorDTO;
import fun.zhub.ppeng.dto.PushRecipeDTO;
import fun.zhub.ppeng.dto.RecipeDTO;
import fun.zhub.ppeng.dto.UpdateRecipeDTO;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.service.RecipeService;
import fun.zhub.ppeng.util.MyBeanUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

import static fun.zhub.ppeng.constant.RabbitConstants.PPENG_EXCHANGE;
import static fun.zhub.ppeng.constant.RabbitConstants.ROUTING_CONTENT_CENSOR;


/**
 * 菜谱接口
 *
 * @author Zaki
 * @since 2023-03-17
 */
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @Resource
    private RecipeService recipeService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 根据菜谱id获取菜谱--仅限服务间调用
     */
    @GetMapping("/info/{recipeId}")
    public Recipe queryRecipeById(@PathVariable("recipeId") Long recipeId) {
        return recipeService.getById(recipeId);
    }

    /**
     * 根据菜谱Id来更新菜谱的点赞数和收藏数--仅限服务间调用
     *
     * @param recipeId 菜谱Id
     * @param field    字段名
     * @param change   变化的数值
     */
    @PostMapping("/update/stats")
    public boolean updateRecipeStatsById(@RequestParam("recipeId") Long recipeId, @RequestParam("field") String field, @RequestParam("change") int change) {
        return recipeService.updateRecipeStatsById(recipeId, field, change);
    }

    /**
     * 菜谱上传
     *
     * @param pushRecipeDTO pushRecipeDTO
     * @return recipeId
     */
    @PostMapping("/push")
    public ResponseResult<String> pushRecipe(@Valid @RequestBody PushRecipeDTO pushRecipeDTO) {
        // 验证id是否和token对应的id一致
        Long id = Long.valueOf((String) StpUtil.getLoginId());
        Long userId = pushRecipeDTO.getUserId();
        if (!Objects.equals(id, userId)) {
            return ResponseResult.fail("id错误");
        }
        Recipe recipe = BeanUtil.copyProperties(pushRecipeDTO, Recipe.class);
        recipe.setIsProfessional(0);
        String urls = String.join(",", pushRecipeDTO.getMediaUrl());
        recipe.setMediaUrl(urls);

        Long recipeId = recipeService.createRecipe(recipe);

        /*
         *  审核菜谱内容
         */
        ContentCensorDTO censorDTO;
        if (recipe.getIsVideo() == 0) {
            censorDTO = new ContentCensorDTO("recipeImages", recipeId, recipe.getTitle(), recipe.getContent(), urls);
        } else {
            censorDTO = new ContentCensorDTO("recipeVideo", recipeId, recipe.getTitle(), urls);
        }
        rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_CONTENT_CENSOR, JSONUtil.toJsonStr(censorDTO));

        return ResponseResult.success(String.valueOf(recipeId));
    }


    /**
     * 根据用户id获取菜谱列表
     *
     * @param userId    用户id
     * @param timestamp 时间戳
     * @param size      一页的菜谱数量
     * @return list
     */
    @GetMapping("/list/by/user")
    public ResponseResult<PageBean<RecipeDTO>> queryRecipeListByUserId(@PathParam("userId") Long userId, @RequestParam(value = "timestamp", defaultValue = "") Long timestamp, @RequestParam(value = "size", defaultValue = "5") Integer size) {
        timestamp = Optional.ofNullable(timestamp).orElseGet(System::currentTimeMillis);

        var pageBean = recipeService.getRecipeListByUserId(userId, timestamp, size);

        return ResponseResult.success(pageBean);
    }


    /**
     * 根据类型id获取菜谱列表
     *
     * @param typeId    菜谱类型id
     * @param timestamp 时间戳
     * @param size      一页的菜谱数量
     * @return list
     */
    @GetMapping("/list/by/type")
    public ResponseResult<PageBean<RecipeDTO>> queryRecipeListByTypeId(@PathParam("typeId") Integer typeId, @RequestParam(value = "timestamp", defaultValue = "") Long timestamp, @RequestParam(value = "size", defaultValue = "5") Integer size) {
        timestamp = Optional.ofNullable(timestamp).orElseGet(System::currentTimeMillis);
        var pageBean = recipeService.getRecipeListByTypeId(typeId, timestamp, size);

        return ResponseResult.success(pageBean);
    }


    /**
     * 获取推荐列表--普通菜谱
     *
     * @param timestamp 最小时间戳
     * @param size      一页的菜谱数量
     * @return RecommendRecipeDTO
     */
    @GetMapping("/recommend/common")
    public ResponseResult<PageBean<RecipeDTO>> queryRecommendCommonRecipeList(@RequestParam(value = "timestamp", defaultValue = "") Long timestamp, @RequestParam(value = "size", defaultValue = "5") Integer size) {
        timestamp = Optional.ofNullable(timestamp).orElseGet(System::currentTimeMillis);
        var pageBean = recipeService.getRecommendRecipeList(0, timestamp, size);
        return ResponseResult.success(pageBean);
    }

    /**
     * 获取推荐列表--专业菜谱
     *
     * @param timestamp 最小时间戳
     * @param size      一页的菜谱数量
     * @return RecommendRecipeDTO
     */
    @GetMapping("/recommend/professional")
    public ResponseResult<PageBean<RecipeDTO>> queryRecommendProfessionalRecipeList(@RequestParam(value = "timestamp", defaultValue = "") Long timestamp, @RequestParam(value = "size", defaultValue = "5") Integer size) {
        timestamp = Optional.ofNullable(timestamp).orElseGet(System::currentTimeMillis);
        var pageBean = recipeService.getRecommendRecipeList(1, timestamp, size);
        return ResponseResult.success(pageBean);
    }


    /**
     * 更新菜谱
     *
     * @param updateRecipeDTO updateRecipeDTO
     * @return success
     */
    @PutMapping("/update")
    public ResponseResult<String> updateRecipe(@Valid @RequestBody UpdateRecipeDTO updateRecipeDTO) {
        Long userId = updateRecipeDTO.getUserId();
        Long id = Long.valueOf((String) StpUtil.getLoginId());
        if (!Objects.equals(id, userId)) {
            return ResponseResult.fail("id错误");
        }

        // 获取recipe对象
        Recipe recipe = recipeService.getRecipeByUserIdAndRecipeId(userId, updateRecipeDTO.getRecipeId());


        // 复制不为null的属性
        MyBeanUtil.copyPropertiesIgnoreNull(updateRecipeDTO, recipe);

        // 格式化mediaUrl
        String mediaUrl = Optional.ofNullable(updateRecipeDTO.getMediaUrl())
                .map(list -> String.join(",", list))
                .orElse(null);

        recipe.setMediaUrl(mediaUrl);

        // 更新recipe
        recipeService.updateRecipe(recipe);

        Long recipeId = recipe.getId();
        String urls = String.join(",", recipe.getMediaUrl());
        /*
         *  审核菜谱内容
         */
        ContentCensorDTO censorDTO;
        if (recipe.getIsVideo() == 0) {
            censorDTO = new ContentCensorDTO("recipeImages", recipeId, recipe.getTitle(), recipe.getContent(), urls);
        } else {
            censorDTO = new ContentCensorDTO("recipeVideo", recipeId, recipe.getTitle(), urls);
        }
        rabbitTemplate.convertAndSend(PPENG_EXCHANGE, ROUTING_CONTENT_CENSOR, JSONUtil.toJsonStr(censorDTO));


        return ResponseResult.success();
    }


    /**
     * 删除菜谱
     *
     * @param recipeId 菜谱id
     * @return success
     */
    @DeleteMapping("/delete/{recipeId}")
    public ResponseResult<String> deleteRecipe(@PathVariable("recipeId") Long recipeId) {
        Long id = Long.valueOf((String) StpUtil.getLoginId());

        recipeService.deleteRecipeById(id, recipeId);

        return ResponseResult.success();
    }


}
