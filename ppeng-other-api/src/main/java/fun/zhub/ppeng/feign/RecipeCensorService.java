package fun.zhub.ppeng.feign;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.RecipeCensorResultDTO;
import fun.zhub.ppeng.feign.impl.RecipeFallCensorBackServiceImpl;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
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

@FeignClient(value = "ppeng-module-recipe", fallback = RecipeFallCensorBackServiceImpl.class)
@Component
public interface RecipeCensorService {

    /**
     * 封禁菜谱，人工审核或者机器审核不通过时调用，如果人工审核不通过，则删除该菜谱
     *
     * @param resultDTO 菜谱审核结果数据传输对象
     * @return success
     */
    @PostMapping("/ban")
    ResponseResult<String> setInaccessible(@RequestBody RecipeCensorResultDTO resultDTO);


    /**
     * 放行菜谱，人工审核或者机器审核通过时调用
     *
     * @param resultDTO 菜谱审核结果数据传输对象
     * @return success
     */
    @PostMapping("/allow")
    ResponseResult<String> setaccessible(@Valid @RequestBody RecipeCensorResultDTO resultDTO);


}
