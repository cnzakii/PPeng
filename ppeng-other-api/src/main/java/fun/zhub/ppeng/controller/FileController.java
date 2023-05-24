package fun.zhub.ppeng.controller;

import fun.zhub.ppeng.common.ResponseResult;
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
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;


    /**
     * 文件上传功能
     *
     * @param type 类型：icon-头像   recipe-images 菜谱图片集合  recipe-video 菜谱视频
     * @param file 上传的文件
     * @return url地址
     */
    @PostMapping("/upload/{type}")
    public ResponseResult<Object> uploadFile(@PathVariable("type") String type, MultipartFile... file) {

        Object url = switch (type) {
            case "icon" -> fileService.uploadUserIcon(file[0]);
            case "recipe-images" -> fileService.uploadRecipeImages(file);
            case "recipe-video" -> fileService.uploadRecipeVideo(file[0]);
            default -> null;
        };

        return ResponseResult.success(url);
    }

}
