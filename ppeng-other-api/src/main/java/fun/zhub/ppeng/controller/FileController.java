package fun.zhub.ppeng.controller;

import com.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.service.FileService;
import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;
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

    /**
     * 文件删除功能
     *
     * @param filePath 文件路径(不包含根路径，包含文件名)
     * @return true or false
     */
    @DeleteMapping("/delete")
    public ResponseResult<Boolean> deleteFile(@PathParam("filePath") String filePath) {
        fileService.deleteFile(filePath);

        return ResponseResult.success();
    }
}
