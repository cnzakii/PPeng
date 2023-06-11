package fun.zhub.ppeng.controller;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.service.ImageRecognitionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 图片识别接口
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-11
 **/
@RestController
@RequestMapping("/image/recognition")
public class ImageRecognitionController {

    @Resource
    private ImageRecognitionService recognitionService;

    /**
     * 菜谱图片识别接口
     *
     * @return 相似菜谱
     */
    @PostMapping("/dish")
    public ResponseResult<Map<String, Double>> dishRecognition(MultipartFile dish) {
        if (dish == null) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "无法读取到有效文件");
        }

        var result = recognitionService.recognizeDish(dish);

        return ResponseResult.success(result);
    }

}
