package fun.zhub.ppeng.controller.handler;

import cn.hutool.core.util.BooleanUtil;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.dto.AddUserMessageDTO;
import fun.zhub.ppeng.dto.RecipeCensorResultDTO;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.feign.MessageService;
import fun.zhub.ppeng.service.RecipeCensorService;
import fun.zhub.ppeng.service.RecipeService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 * 菜谱违规信息处理接口
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-25
 **/
@RestController
@RequestMapping("/handler/recipe")
@Slf4j
public class RecipeCensorHandler {

    @Resource
    private RecipeService recipeService;

    @Resource
    private RecipeCensorService censorService;

    @Resource
    private MessageService messageService;


    /**
     * 封禁菜谱，人工审核或者机器审核不通过时调用，如果人工审核不通过，则删除该菜谱
     *
     * @param resultDTO 菜谱审核结果数据传输对象
     * @return success
     */
    @PostMapping("/ban")
    public ResponseResult<String> setInAccessible(@Valid @RequestBody RecipeCensorResultDTO resultDTO) {
        Long recipeId = resultDTO.getRecipeId();
        // 根据recipeId获取当前菜谱
        Recipe recipe = recipeService.getById(recipeId);
        Long userId = recipe.getUserId();

        Integer censorState = resultDTO.getCensorState();


        recipeService.updateCensorState(recipeId, censorState, 1);
        // 如果人工复审（CensorState=3）仍不通过审核，则进一步执行删除操作
        if (censorState == 3) {
            boolean b = recipeService.removeById(recipeId);
            if (!b) {
                return ResponseResult.fail("删除菜谱失败");
            }
        }

        // 将审核结果保存
        String censorResult = resultDTO.getCensorResult();
        Long censorId = resultDTO.getCensorId();
        LocalDateTime censorTime = resultDTO.getCensorTime();
        censorService.saveCensorResult(recipeId, censorResult, censorId, censorTime, censorState);

        /*
         * 调用消息模块，通知用户
         */
        /*
         *TODO 获取人工复审链接
         */
        String title = "菜谱违规通知";
        String content;
        if (censorState == 3) {
            content = "尊敬的用户，您的菜谱存在违规内容，违规原因是" + censorResult + "。结合多次审核结果，我们不再接受关于该菜谱的申述处理，并永久删除该菜谱。感谢您的配合！";
        } else {
            content = "尊敬的用户，您的菜谱存在违规内容，违规原因是" + censorResult + "。如果你对此有异议，请点击下方链接进行人工复审，我们将在7个工作日回复您。感谢您的配合！";
        }
        AddUserMessageDTO addUserMessageDTO = new AddUserMessageDTO(userId, title, content);

        Boolean a = messageService.addMeaasge(addUserMessageDTO);

        if (BooleanUtil.isFalse(a)) {
            log.error("发送({})违规通知失败", userId);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "发送违规通知失败");
        }

        return ResponseResult.success();
    }

    /**
     * 放行菜谱，人工审核或者机器审核通过时调用
     *
     * @param resultDTO 菜谱审核结果数据传输对象
     * @return success
     */
    @PostMapping("/allow")
    public ResponseResult<String> setaccessible(@Valid @RequestBody RecipeCensorResultDTO resultDTO) {
        Integer censorState = resultDTO.getCensorState();
        Long recipeId = resultDTO.getRecipeId();


        // 根据recipeId获取当前菜谱
        Recipe recipe = recipeService.getById(recipeId);
        Long userId = recipe.getUserId();

        recipeService.updateCensorState(recipeId, censorState, 0);


        // 将审核结果保存
        String censorResult = resultDTO.getCensorResult();
        Long censorId = resultDTO.getCensorId();
        LocalDateTime censorTime = resultDTO.getCensorTime();
        censorService.saveCensorResult(recipeId, censorResult, censorId, censorTime, censorState);

        /*
         * 调用消息模块，通知用户
         */
        String title = "菜谱审核通过通知";
        String content = "尊敬的用户，恭喜您！您的菜谱已通过审核，符合我们的社区标准和规定。感谢您的分享和支持！继续创作精彩的菜谱，为我们的社区带来更多美味的享受吧！祝您继续成功！";

        AddUserMessageDTO addUserMessageDTO = new AddUserMessageDTO(userId, title, content);

        Boolean a = messageService.addMeaasge(addUserMessageDTO);

        if (BooleanUtil.isFalse(a)) {
            log.error("发送({})违规通知失败", userId);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "发送违规通知失败");
        }

        return ResponseResult.success();
    }


}
