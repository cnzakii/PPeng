package com.zhub.ppeng.exception;


import com.zhub.ppeng.common.ResponseStatus;

/**
 * Business exception
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-17
 */
public class BusinessException extends RuntimeException {

    private final String code;

    private final String description;

    public BusinessException(String message, String code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ResponseStatus errorCode) {
        this.code = errorCode.getResponseCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ResponseStatus errorCode, String description) {
        this.code = errorCode.getResponseCode();
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
