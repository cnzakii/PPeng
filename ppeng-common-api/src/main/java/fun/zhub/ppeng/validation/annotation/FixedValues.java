package fun.zhub.ppeng.validation.annotation;

import fun.zhub.ppeng.validation.validator.FixedValuesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 校验Integer数值只能是某一组固定的数组
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
@Documented
@Constraint(validatedBy = {FixedValuesValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FixedValues {
    int[] values();

    String message() default "Invalid value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        FixedValues[] value();
    }
}
