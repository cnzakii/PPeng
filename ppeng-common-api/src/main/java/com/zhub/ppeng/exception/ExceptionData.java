package com.zhub.ppeng.exception;


import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * <p>
 * 异常数据列表
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-17
 **/
@Data
@Builder
public class ExceptionData {

    @Singular
    private final List<Object> errors;

}