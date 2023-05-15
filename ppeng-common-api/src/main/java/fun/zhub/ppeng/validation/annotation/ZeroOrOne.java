package fun.zhub.ppeng.validation.annotation;

import fun.zhub.ppeng.validation.validator.ZeroOrOneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 校验数值只能是0或者1
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
@Documented
@Constraint(validatedBy = {ZeroOrOneValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ZeroOrOne {
    String message() default "Value must be 0 or 1";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ZeroOrOne[] value();
    }
}