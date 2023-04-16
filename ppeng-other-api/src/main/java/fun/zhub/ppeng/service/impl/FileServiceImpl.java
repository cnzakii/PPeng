package fun.zhub.ppeng.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.zhub.ppeng.common.ResponseStatus;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.zhub.ppeng.constant.SystemConstants.FILE_ICON_SUB_PATH;
import static com.zhub.ppeng.constant.SystemConstants.FILE_ROOT_PATH;

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
    public String updateUserIcon(MultipartFile icon) {
        String path = updateFile(FILE_ICON_SUB_PATH, icon);

        if (StrUtil.isEmpty(path)) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "头像保存失败");
        }

        return path;
    }


    /**
     * 实现文件上传功能
     *
     * @param file 文件
     * @return 路径(不包含根路径)
     */
    @Override
    public String updateFile(String subPath, MultipartFile file) {
        String oldFileName = file.getOriginalFilename();

        if (StrUtil.isEmpty(oldFileName)) {
            log.warn("文件名为空");
            return null;
        }

        String newFileName = UUID.randomUUID().toString(true) + oldFileName.substring(oldFileName.lastIndexOf("."));

        File dest = new File(FILE_ROOT_PATH + subPath + newFileName);

        // 判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            boolean b = dest.getParentFile().mkdir();
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
        if (file.exists()) {
            boolean deleted = file.delete();
            if (BooleanUtil.isFalse(deleted)) {
                log.error("文件删除失败，filePath===》{}", path);
                throw new BusinessException(ResponseStatus.HTTP_STATUS_500,"文件删除失败");
            }
        }
        log.warn("文件不存在");
        throw new BusinessException(ResponseStatus.HTTP_STATUS_400,"文件不存在");
    }


}
