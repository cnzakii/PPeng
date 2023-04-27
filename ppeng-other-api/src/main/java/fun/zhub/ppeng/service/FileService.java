package fun.zhub.ppeng.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 文件处理interface
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-16
 **/
public interface FileService {


    /**
     * 上传用户头像
     *
     * @param icon 头像
     * @return 地址
     */
    String uploadUserIcon(MultipartFile icon);

    /**
     * 上传菜谱图片集
     *
     * @param images 文件
     * @return url数组
     */
    String[] uploadRecipeImages(MultipartFile[] images);

    /**
     * 上传菜谱视频
     *
     * @param video 视频
     * @return url
     */
    String uploadRecipeVideo(MultipartFile video);


    /**
     * 文件上传通用方法
     *
     * @param subPath 子路径
     * @param file    文件
     * @return 文件路径（不包含根路径）
     */
    String uploadFile(String subPath, MultipartFile file);

    /**
     * 文件删除通用方法
     *
     * @param filePath 文件路径（包含文件名，不包含根路径）
     */
    void deleteFile(String filePath);


}
