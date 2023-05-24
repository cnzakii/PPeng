package fun.zhub.ppeng.feign;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.feign.impl.RecipeFallbackServiceImpl;
import fun.zhub.ppeng.feign.interceptor.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * OpenFeign调用RecipeService 服务
 *
 * @author Zaki
 * @version 2.0
 * @since 2023-05-24
 **/
@Component
@FeignClient(value = "ppeng-module-recipe", fallback = RecipeFallbackServiceImpl.class, configuration = FeignInterceptor.class)
public interface RecipeService {
    /**
     * 根据id获取菜谱类型名字
     *
     * @param id 菜谱类型id
     * @return 菜谱类型名字
     */
    @GetMapping("/recipe/type/name")
    ResponseResult<String> getNameById(@RequestParam("id") Integer id);
}
