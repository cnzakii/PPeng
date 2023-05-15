package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.*;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.service.RecipeService;
import fun.zhub.ppeng.util.MyBeanUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
     * @param userId 用户id
     * @param page   当前页数
     * @param size   一页的菜谱数量
     * @return list
     */
    @GetMapping("/list/by/user")
    public ResponseResult<List<RecipeDTO>> queryRecipeListByUserId(@PathParam("userId") Long userId, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "5") Integer size) {

        var list = recipeService.getRecipeListByUserId(userId, page, size)
                .stream()
                .map(recipe -> BeanUtil.copyProperties(recipe, RecipeDTO.class))
                .toList();

        return ResponseResult.success(list);
    }


    /**
     * 根据类型id获取菜谱列表
     *
     * @param typeId 菜谱类型id
     * @param page   当前页数
     * @param size   一页的菜谱数量
     * @return list
     */
    @GetMapping("/list/by/type")
    public ResponseResult<List<RecipeDTO>> queryRecipeListByTypeId(@PathParam("typeId") Integer typeId, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "5") Integer size) {

        var list = recipeService.getRecipeListByTypeId(typeId, page, size)
                .stream()
                .map(recipe -> BeanUtil.copyProperties(recipe, RecipeDTO.class))
                .toList();

        return ResponseResult.success(list);
    }


    /**
     * 获取推荐列表--普通菜谱
     *
     * @param lastTimestamp 最小时间戳
     * @param size          一页的菜谱数量
     * @return RecommendRecipeDTO
     */
    @GetMapping("/recommend/common")
    public ResponseResult<RecommendRecipeDTO> queryRecommendCommonRecipeList(@RequestParam(value = "lastTimestamp") Long lastTimestamp, @RequestParam(value = "size", defaultValue = "5") Integer size) {

        RecommendRecipeDTO dto = recipeService.getRecommendRecipeList(0, lastTimestamp, size);
        return ResponseResult.success(dto);
    }

    /**
     * 获取推荐列表--专业菜谱
     *
     * @param lastTimestamp 最小时间戳
     * @param size          一页的菜谱数量
     * @return RecommendRecipeDTO
     */
    @GetMapping("/recommend/professional")
    public ResponseResult<RecommendRecipeDTO> queryRecommendProfessionalRecipeList(@RequestParam(value = "lastTimestamp") Long lastTimestamp, @RequestParam(value = "size", defaultValue = "5") Integer size) {

        RecommendRecipeDTO dto = recipeService.getRecommendRecipeList(1, lastTimestamp, size);
        return ResponseResult.success(dto);
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
