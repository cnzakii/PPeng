package fun.zhub.ppeng.exception;


import com.zhub.ppeng.common.ResponseResult;
import com.zhub.ppeng.exception.BusinessException;

import io.micrometer.core.instrument.config.validate.ValidationException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zhub.ppeng.common.ResponseStatus.HTTP_STATUS_400;

/**
 * <p>
 * Global exception handler
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-17
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @InitBinder
    public void handleInitBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));
    }

    /**
     * 处理参数验证异常
     *
     * @param e exception
     * @return ResponseResult
     */
    @ResponseBody
    @ExceptionHandler(value = {
            BindException.class,
            ValidationException.class
    })
    public ResponseResult<String> handleParameterVerificationException(Exception e) {

        List<String> exceptionMsg = new ArrayList<>();

        log.error("Exception: {}", e.getMessage());

        if (e instanceof BindException) {
            BindingResult bindingResult = ((BindException) e).getBindingResult();
            bindingResult.getAllErrors()
                    .forEach(a -> exceptionMsg.add(a.getDefaultMessage()));
        } else if (e instanceof ConstraintViolationException) {
            if (e.getMessage() != null) {
                exceptionMsg.add(e.getMessage());
            }
        } else {
            exceptionMsg.add("invalid parameter");
        }

        return ResponseResult.base(HTTP_STATUS_400, null, exceptionMsg.toString());
    }


    /**
     * 处理业务异常
     *
     * @param businessException business exception
     * @return ResponseResult
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public ResponseResult<BusinessException> processBusinessException(BusinessException businessException) {

        log.error("ResponseCode：{},Exception: {}", businessException.getCode(), businessException.getDescription());
        businessException.printStackTrace();
        return ResponseResult.fail(businessException.getDescription());
    }


    /**
     * 处理其他异常
     *
     * @param exception exception
     * @return ResponseResult
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseResult<Exception> processException(Exception exception) {
        log.error("Exception: {}", exception.getMessage());
        // 这里可以屏蔽掉后台的异常栈信息，直接返回"server error"
        return ResponseResult.fail(exception.getLocalizedMessage());
    }
}