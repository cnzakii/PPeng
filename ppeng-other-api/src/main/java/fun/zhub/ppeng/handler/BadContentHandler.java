package fun.zhub.ppeng.handler;

import com.zhub.ppeng.common.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>
 * 利用OpenFeign调用其他模块 处理内容违规
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-14
 **/
@FeignClient(value = "ppeng-module-user", fallback = BadContentFallbackHandler.class)
@Component
public interface BadContentHandler {

    /**
     * 处理昵称违规
     *
     * @param id 用户id
     * @return success
     */
    @PostMapping("/handle/user/nick/name/{id}")
    ResponseResult<String> handleBadNickName(@PathVariable("id") Long id);

}
