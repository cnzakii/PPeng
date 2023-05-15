package fun.zhub.ppeng.feign;

import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.feign.interceptor.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * OpenFeign调用recipe模块
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/

@FeignClient(value = "ppeng-module-recipe", fallback = RecipeService.class, configuration = FeignInterceptor.class)
@Component
public interface RecipeService {

    @GetMapping("/info/{recipeId}")
    Recipe queryRecipeById(@PathVariable("recipeId") Long recipeId);
}
