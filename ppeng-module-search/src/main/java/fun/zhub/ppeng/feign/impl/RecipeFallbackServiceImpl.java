package fun.zhub.ppeng.feign.impl;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.feign.RecipeService;

/**
 * RecipeService 调用失败的兜底方法
 *
 * @author Zaki
 * @version 2.0
 * @since 2023-05-24
 **/
public class RecipeFallbackServiceImpl implements RecipeService {
    @Override
    public ResponseResult<String> getNameById(Integer id) {
        return ResponseResult.fail("RecipeService服务调用失败");
    }
}
