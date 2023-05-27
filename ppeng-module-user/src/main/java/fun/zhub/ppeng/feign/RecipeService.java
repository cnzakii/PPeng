package fun.zhub.ppeng.feign;

import fun.zhub.ppeng.feign.impl.RecipeFallbackServiceImpl;
import fun.zhub.ppeng.feign.interceptor.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * OpenFeign调用RecipeService 服务
 *
 * @author Zaki
 * @version 2.0
 * @since 2023-05-26
 **/
@Component
@FeignClient(value = "ppeng-module-recipe", fallback = RecipeFallbackServiceImpl.class, configuration = FeignInterceptor.class)
public interface RecipeService {
    /**
     * 根据菜谱Id来更新菜谱的点赞数和收藏数--仅限服务间调用
     *
     * @param recipeId 菜谱Id
     * @param field    字段名
     * @param change   变化的数值
     */
    @PostMapping("/recipe/update/stats")
    boolean updateRecipeStatsById(@RequestParam("recipeId") Long recipeId, @RequestParam("field") String field, @RequestParam("change") int change);
}
