package fun.zhub.ppeng.feign;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.feign.impl.CallRecipeFallBackServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>
 * 利用OpenFeign调用recipe模块 处理内容违规
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-25
 **/

@FeignClient(value = "ppeng-module-recipe", fallback = CallRecipeFallBackServiceImpl.class)
public interface CallRecipeService {

    /**
     * 封禁菜谱，人工审核或者机器审核不通过时调用，如果人工审核不通过，则删除该菜谱
     *
     * @param type     类型：admin 人工审核 ai 机器审核
     * @param recipeId 菜谱id
     * @return success
     */
    @PostMapping("/ban/{type}/{recipeId}")
    ResponseResult<String> setInaccessible(@PathVariable("type") String type, @PathVariable("recipeId") Long recipeId);




}
