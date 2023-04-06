package fun.zhub.ppeng.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>
 *  文本审核接口
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-06
 **/

@FeignClient(url = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined",name = "baidu")
public interface CheckTextService {

   

}
