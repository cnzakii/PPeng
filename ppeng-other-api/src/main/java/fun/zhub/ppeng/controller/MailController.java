package fun.zhub.ppeng.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhub.ppeng.common.ResponseResult;
import com.zhub.ppeng.common.ResponseStatus;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.service.MailService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

import static com.zhub.ppeng.constant.RedisConstants.REGISTER_CODE_KEY;
import static com.zhub.ppeng.constant.RedisConstants.REGISTER_CODE_TTL;
import static fun.zhub.ppeng.constants.MailConstants.*;

/**
 * <p>
 * 发送邮箱
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-08
 **/

@RestController
@RequestMapping("/mail")
public class MailController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MailService mailService;

    @PostMapping("/register")
    public ResponseResult<String> sendRegisterMail(@RequestBody String mail) {
        JSONObject obj = JSONUtil.parseObj(mail);
        String smail = (String) obj.get("mail");
        // 先查询redis中是否存在该信息
        String key = REGISTER_CODE_KEY + smail;

        String s = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotEmpty(s)) {
            throw new BusinessException(ResponseStatus.FAIL, "已向该邮箱发送验证，请勿频繁重试");
        }

        // 生成6位长度的验证码，发送邮件并写入Redis
        String code = RandomUtil.randomString(6);

        mailService.sendSimpleMail(MAIL_FROM, MAIL_FROM_NICK, smail, "", REGISTER_SUBJECT, REGISTER_CONTENT_PREFIX + code + REGISTER_CONTENT_SUFFIX);

        stringRedisTemplate.opsForValue().set(key, code, REGISTER_CODE_TTL, TimeUnit.MINUTES);

        return ResponseResult.success(smail);
    }
}
