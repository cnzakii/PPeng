package fun.zhub.ppeng.feign;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.RecipeCensorResultDTO;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.entity.RecipeCensor;
import fun.zhub.ppeng.feign.impl.RecipeCensorFallBackServiceImpl;
import fun.zhub.ppeng.feign.interceptor.FeignInterceptor;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 利用OpenFeign调用recipe模块 处理内容违规
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-25
 **/

@FeignClient( value = "ppeng-module-recipe", fallback = RecipeCensorFallBackServiceImpl.class, configuration = FeignInterceptor.class)
@Component
public interface RecipeCensorService {

    /**
     * 根据菜谱id获取菜谱对象
     *
     * @param recipeId 菜谱id
     * @return recipe
     */
    @GetMapping("/info/{recipeId}")
    Recipe queryRecipeById(@PathVariable("recipeId") Long recipeId);

    /**
     * 根据菜谱id获取审查结果---仅限服务间调用
     */
    @GetMapping("/info/{recipeId}")
    RecipeCensor getRecipeCensorById(@PathVariable("recipeId") Long recipeId);

    /**
     * 封禁菜谱，人工审核或者机器审核不通过时调用，如果人工审核不通过，则删除该菜谱
     *
     * @param resultDTO 菜谱审核结果数据传输对象
     * @return success
     */
    @PostMapping("/recipe/censor/ban")
    ResponseResult<String> setInaccessible(@RequestBody RecipeCensorResultDTO resultDTO);


    /**
     * 放行菜谱，人工审核或者机器审核通过时调用
     *
     * @param resultDTO 菜谱审核结果数据传输对象
     * @return success
     */
    @PostMapping("/recipe/censor/allow")
    ResponseResult<String> setaccessible(@Valid @RequestBody RecipeCensorResultDTO resultDTO);


}
