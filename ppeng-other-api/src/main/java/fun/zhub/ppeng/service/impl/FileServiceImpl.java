package fun.zhub.ppeng.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static fun.zhub.ppeng.constant.SystemConstants.FILE_ROOT_PATH;
import static fun.zhub.ppeng.constant.SystemConstants.PPENG_RESOURCE_URL;

/**
 * <p>
 * FileService 实现类
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-16
 **/
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    /**
     * 实现头像上传功能
     *
     * @param icon 头像
     * @return 头像路径
     */
    @Override
    public String uploadUserIcon(MultipartFile icon) {
        LocalDate localDate = LocalDate.now();

        String subPath = "/icon/" + localDate.getYear() + "/" + localDate.getMonthValue() + "/" + localDate.getDayOfMonth() + "/";

        String path = uploadFile(subPath, icon);


        if (StrUtil.isEmpty(path)) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "头像保存失败");
        }

        return path;
    }

    /**
     * 实现上传菜谱图片集功能
     *
     * @param images 图片集
     * @return url数组
     */
    @Override
    public String[] uploadRecipeImages(MultipartFile[] images) {
        LocalDate localDate = LocalDate.now();

        String subPath = "/recipe/image/" + localDate.getYear() + "/" + localDate.getMonthValue() + "/" + localDate.getDayOfMonth() + "/";


        List<String> urls = Arrays.stream(images)
                .map(image -> uploadFile(subPath, image))
                .filter(StrUtil::isNotEmpty)
                .map(path -> PPENG_RESOURCE_URL + path)
                .toList();

        if (urls.size() != images.length) {
            urls.forEach(url -> deleteFile(url.replace(PPENG_RESOURCE_URL, "")));
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "菜谱图像保存失败");
        }

        return urls.toArray(new String[0]);
    }

    /**
     * 实现菜谱视频上传功能
     *
     * @param video 视频
     * @return url
     */
    @Override
    public String uploadRecipeVideo(MultipartFile video) {
        LocalDate localDate = LocalDate.now();

        String subPath = "/recipe/video/" + localDate.getYear() + "/" + localDate.getMonthValue() + "/" + localDate.getDayOfMonth() + "/";

        String path = uploadFile(subPath, video);

        if (StrUtil.isEmpty(path)) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "菜谱视频保存失败");
        }

        return PPENG_RESOURCE_URL + path;
    }


    /**
     * 实现文件上传功能
     *
     * @param file 文件
     * @return 路径(不包含根路径)
     */
    @Override
    public String uploadFile(String subPath, MultipartFile file) {
        String oldFileName = file.getOriginalFilename();

        if (StrUtil.isEmpty(oldFileName)) {
            log.warn("文件名为空");
            return null;
        }


        String newFileName = IdUtil.simpleUUID() + oldFileName.substring(oldFileName.lastIndexOf("."));

        File dest = new File(FILE_ROOT_PATH + subPath + newFileName);

        // 判断文件目录是否存在
        if (!dest.getParentFile().exists()) {
            log.info("创建目录{}", dest.getParentFile());
            boolean b = dest.getParentFile().mkdirs();
            if (!b) {
                log.error("目录({})创建失败", dest.getParentFile());
                return null;
            }
        }

        try {
            // 保存文件
            file.transferTo(dest);
        } catch (IOException exception) {
            log.error("文件保存失败=====》{}", exception.getLocalizedMessage());
            return null;
        }


        return subPath + newFileName;
    }


    /**
     * 实现文件删除功能
     *
     * @param filePath 文件路径（包含文件名，不包含根路径）
     */
    @Override
    public void deleteFile(String filePath) {
        String path = FILE_ROOT_PATH + filePath;
        File file = new File(path);

        if (!file.exists()) {
            log.warn("文件{}不存在", file);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "文件不存在");
        }


        boolean deleted = file.delete();
        if (BooleanUtil.isFalse(deleted)) {
            log.error("文件删除失败，filePath===》{}", path);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "文件删除失败");
        }


    }


}
