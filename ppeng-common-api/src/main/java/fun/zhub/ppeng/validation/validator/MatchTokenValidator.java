package fun.zhub.ppeng.validation.validator;

import cn.dev33.satoken.stp.StpUtil;
import fun.zhub.ppeng.validation.annotation.MatchToken;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;


/**
 * 注解MatchToken 的Validator
 * 校验token解析出的id是否和传入用户id是否一致<br>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-14
 **/
public class MatchTokenValidator implements ConstraintValidator<MatchToken, Long> {
    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext constraintValidatorContext) {


        Long id = Long.valueOf((String) StpUtil.getLoginId());
        return Objects.equals(id, userId);
    }
}
