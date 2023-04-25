package fun.zhub.ppeng.feign.impl;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.feign.CallRecipeService;

/**
 * <p>
 * CallRecipeService 调用失败的兜底方法
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-25
 **/
public class CallRecipeFallBackServiceImpl implements CallRecipeService {
    @Override
    public ResponseResult<String> setInaccessible(String type, Long recipeId) {
        return ResponseResult.fail("调用recipe模块失败");
    }
}
