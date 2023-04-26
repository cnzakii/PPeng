package fun.zhub.ppeng.feign.impl;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.RecipeCensorResultDTO;
import fun.zhub.ppeng.feign.CallRecipeCensorService;

/**
 * <p>
 * CallRecipeService 调用失败的兜底方法
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-25
 **/
public class CallRecipeFallCensorBackServiceImpl implements CallRecipeCensorService {

    @Override
    public ResponseResult<String> setInaccessible(RecipeCensorResultDTO resultDTO) {
        return ResponseResult.fail("recipe模块调用失败");

    }

    @Override
    public ResponseResult<String> setaccessible(RecipeCensorResultDTO resultDTO) {
        return ResponseResult.fail("recipe模块调用失败");
    }
}
