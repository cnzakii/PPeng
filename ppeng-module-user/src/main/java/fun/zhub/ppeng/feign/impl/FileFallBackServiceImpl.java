package fun.zhub.ppeng.feign.impl;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.feign.FileService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileService 服务调用失败处理类
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-04
 **/
@Component
public class FileFallBackServiceImpl implements FileService {

    @Override
    public ResponseResult<String> uploadFile( MultipartFile... file) {
        return ResponseResult.fail("文件上传服务调用失败");
    }

}
