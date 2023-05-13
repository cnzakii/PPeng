package fun.zhub.ppeng.feign;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.feign.impl.FileFallBackService;
import fun.zhub.ppeng.feign.interceptor.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件处理接口
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-04
 **/
@Component
@FeignClient(value = "ppeng-other-api", fallback = FileFallBackService.class, configuration = FeignInterceptor.class)
public interface FileService {

    /**
     * 上传用户头像
     *
     * @param file 头像文件
     * @return success
     */
    @PostMapping("/file/upload/icon")
    ResponseResult<String> uploadFile(MultipartFile... file);

}
