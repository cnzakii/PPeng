package fun.zhub.ppeng.validation.validator;

import fun.zhub.ppeng.validation.annotation.ZeroOrOne;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 注解ZeroOrOne 的Validator<br>
 * 用于校验Integer类型的数值仅能为0或者1
 *
 * @author Zaki
 * @version TODO
 * @since 2023-05-15
 **/
public class ZeroOrOneValidator implements ConstraintValidator<ZeroOrOne, Integer> {


    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && (value == 0 || value == 1);
    }
}
