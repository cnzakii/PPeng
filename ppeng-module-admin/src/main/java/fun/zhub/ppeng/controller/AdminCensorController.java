package fun.zhub.ppeng.controller;

import cn.hutool.core.util.StrUtil;
import fun.zhub.ppeng.common.PageBean;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.dto.ManualCensorDTO;
import fun.zhub.ppeng.dto.ManualCensorResultDTO;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.service.AdminCensorService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static fun.zhub.ppeng.constant.RedisConstants.APPEAL_RECIPE_KEY;
import static fun.zhub.ppeng.constant.RedisConstants.REPORT_RECIPE_KEY;

/**
 * 管理员审查菜谱接口
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
@RestController
@RequestMapping("/hidden/admin/censor")
public class AdminCensorController {

    @Resource
    private AdminCensorService adminCensorService;


    /**
     * 获取被举报/用户申述的菜谱列表
     *
     * @return list
     */
    @GetMapping("/list/{type}")
    public ResponseResult<PageBean<ManualCensorDTO>> getReportedRecipeList(@PathVariable("type") String type, @RequestParam(value = "timestamp", defaultValue = "") Long timestamp, @RequestParam(value = "size", defaultValue = "10") Integer size) {
        timestamp = Optional.ofNullable(timestamp).orElseGet(System::currentTimeMillis);
        String key;
        // 获取key
        if (StrUtil.equals(type, "reported")) {
            key = REPORT_RECIPE_KEY;
        } else if (StrUtil.equals(type, "appeal")) {
            key = APPEAL_RECIPE_KEY;
        } else {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "type错误");
        }

        var list = adminCensorService.getReportedRecipeList(timestamp, size, key);

        return ResponseResult.success(list);
    }


    /**
     * 处理举报/申述请求
     *
     * @return success
     */
    @PostMapping("/handle/{type}reported")
    public ResponseResult<String> handleReportedRecipe(@PathVariable("type") String type, @RequestBody @Validated ManualCensorResultDTO resultDTO) {
        String key;
        int isNotify = 0;
        // 获取key
        if (StrUtil.equals(type, "reported")) {
            key = REPORT_RECIPE_KEY;
        } else if (StrUtil.equals(type, "appeal")) {
            key = APPEAL_RECIPE_KEY;
            isNotify = 1;
        } else {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "type错误");
        }

        Long adminId = resultDTO.getAdminId();
        Long recipeId = resultDTO.getRecipeId();
        Integer result = resultDTO.getResult();
        String reason = resultDTO.getReason();

        // 处理审核结果,并选择没有违规时不通知用户
        adminCensorService.handlerRecipeCensorResult(adminId, recipeId, result, reason, isNotify, key);


        return ResponseResult.success();
    }

}
