package fun.zhub.ppeng.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 返回内容封装
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-17
 **/


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseResult<T> implements Serializable {
    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 状态码, 200 -> OK.
     */
    private String status;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 响应成功结果包装器(无数据)
     *
     * @param <T> data类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success() {
        return success(null);
    }

    /**
     * 响应成功结果包装器(有数据)
     *
     * @param data data
     * @param <T>  data类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success(T data) {
        return ResponseResult.<T>builder().data(data)
                .message(ResponseStatus.SUCCESS.getDescription())
                .status(ResponseStatus.SUCCESS.getResponseCode())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 响应错误结果包装器(无数据)
     *
     * @param message 错误信息
     * @param <T>     data类型
     * @return 响应结果
     */
    public static <T extends Serializable> ResponseResult<T> fail(String message) {
        return fail(null, message);
    }

    /**
     * 响应错误结果包装器(有数据)
     *
     * @param data    响应数据
     * @param message 错误信息
     * @param <T>     data类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> fail(T data, String message) {
        return ResponseResult.<T>builder().data(data)
                .message(message)
                .status(ResponseStatus.FAIL.getResponseCode())
                .timestamp(System.currentTimeMillis())
                .build();
    }


    /**
     * 自定义返回结果
     *
     * @param status  状态码
     * @param data    数据
     * @param message 信息
     * @param <T>     data类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> base(ResponseStatus status, T data, String message) {
        return base(status.getResponseCode(),data,message);
    }

    /**
     * 自定义返回结果
     *
     * @param status  ResponseStatus
     * @param message 信息
     * @param <T>T
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> base(ResponseStatus status, String message) {
        return base(status, null, message);
    }


    /**
     * 自定义返回结果
     *
     * @param status ResponseStatus
     * @param <T>T
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> base(ResponseStatus status) {
        return base(status, null, status.getDescription());
    }


    /**
     * 自定义返回结果
     *
     * @param code    状态码
     * @param data    数据
     * @param message 信息
     * @param <T>     T
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> base(String code, T data, String message) {
        return ResponseResult.<T>builder().data(data)
                .message(message)
                .status(code)
                .timestamp(System.currentTimeMillis())
                .build();
    }


}
