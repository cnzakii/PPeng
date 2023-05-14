package fun.zhub.ppeng.validation.annotation;

import fun.zhub.ppeng.validation.validator.MatchTokenValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 用于 token和用户id的匹配<br>
 * 校验token解析出的id是否和传入用户id是否一致
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-14
 **/
@Documented
@Constraint(validatedBy = {MatchTokenValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchToken {

    String message() default "id与token不一致";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        MatchToken[] value();
    }

}
