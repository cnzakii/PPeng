package fun.zhub.ppeng.exception;

import com.zhub.ppeng.common.ResponseResult;
import com.zhub.ppeng.exception.BusinessException;
import com.zhub.ppeng.exception.ExceptionData;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

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
     * exception handler for bad request.(400)
     * Include: BindException, ValidationException and MethodArgumentNotValidException
     *
     * @param e exception
     * @return ResponseResult
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {
            BindException.class,
            ValidationException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseResult<ExceptionData> handleParameterVerificationException(Exception e) {

        ExceptionData.ExceptionDataBuilder exceptionDataBuilder = ExceptionData.builder();
        log.error("Exception: ", e);
        if (e instanceof BindException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            bindingResult.getAllErrors()
                    .forEach(a -> exceptionDataBuilder.error(((FieldError) a).getField() + ": " + a.getDefaultMessage()));
        } else if (e instanceof ConstraintViolationException) {
            if (e.getMessage() != null) {
                exceptionDataBuilder.error(e.getMessage());
            }
        } else {
            exceptionDataBuilder.error("invalid parameter");
        }
        return ResponseResult.fail(exceptionDataBuilder.build(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }


    /**
     * handle business exception.
     *
     * @param businessException business exception
     * @return ResponseResult
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public ResponseResult<BusinessException> processBusinessException(BusinessException businessException) {
        log.error(businessException.getLocalizedMessage(), businessException);
        // 这里可以屏蔽掉后台的异常栈信息，直接返回"business error"
        return ResponseResult.fail(businessException, businessException.getLocalizedMessage());
    }

    /**
     * handle other exception.
     *
     * @param exception exception
     * @return ResponseResult
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseResult<Exception> processException(Exception exception) {
        log.error(exception.getLocalizedMessage(), exception);
        // 这里可以屏蔽掉后台的异常栈信息，直接返回"server error"
        return ResponseResult.fail(exception, exception.getLocalizedMessage());
    }
}