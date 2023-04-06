package fun.zhub.ppeng.controller;

import com.zhub.ppeng.common.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 文本审核模块
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-06
 **/


@RestController
@RequestMapping("/check")
public class CheckController {



    /**
     * 使用百度api审核文本内容
     *
     * @return 结果
     */
    @PostMapping("/text")
    public ResponseResult<String> reviewTextByBaidu(String text) {







        return ResponseResult.success(null);
    }
}
