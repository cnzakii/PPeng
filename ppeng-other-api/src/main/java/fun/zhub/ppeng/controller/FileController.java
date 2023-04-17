package fun.zhub.ppeng.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.exception.GlobalBlockHandler;
import fun.zhub.ppeng.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 文件上传和删除模块
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-16
 **/

@RestController
@RequestMapping("file")
public class FileController {

    @Resource
    private FileService fileService;


    /**
     * 文件上传功能
     *
     * @param type 类型：icon-头像   recipe-菜谱
     * @param file 上传的文件
     * @return url地址
     */
    @PostMapping("/upload/{type}")
    @SentinelResource(value = "uploadFile", blockHandlerClass = GlobalBlockHandler.class, blockHandler = "handleCommonBlockException")
    public ResponseResult<String> uploadFile(@PathVariable("type") String type, MultipartFile file) {

        String url = switch (type) {
            case "icon" -> fileService.updateUserIcon(file);
            /*
             * TODO 上传菜谱图片
             */
            case "recipe" -> null;

            default -> null;
        };

        return ResponseResult.success(url);
    }

}
