package fun.zhub.ppeng.validation.validator;

import fun.zhub.ppeng.validation.annotation.FixedValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

/**
 * 注解FixedValues的Validator<br>
 * 校验Integer数值只能是某一组固定的数组
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
public class FixedValuesValidator implements ConstraintValidator<FixedValues, Integer> {
    private int[] allowedValues;

    @Override
    public void initialize(FixedValues constraintAnnotation) {
        allowedValues = constraintAnnotation.values();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && Arrays.stream(allowedValues).anyMatch(element -> element == value);
    }
}
