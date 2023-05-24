package fun.zhub.ppeng.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * <p>
 * 枚举类 常见的状态码
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-17
 **/

@Getter
@AllArgsConstructor
public enum ResponseStatus {


    SUCCESS("200", "success"),
    FAIL("501", "failed"),

    HTTP_STATUS_200("200", "OK"), // 请求成功
    HTTP_STATUS_400("400", "Request Error"), // 请求错误
    HTTP_STATUS_401("401", "No Authentication"), // 没有认证(token)
    HTTP_STATUS_403("403", "No Authorities"), // 没有权限
    HTTP_STATUS_500("500", "Server Error"), // 服务器内部错误
    HTTP_STATUS_429("429", "Too Many Requests");// 过多请求

    public static final List<ResponseStatus> HTTP_STATUS_ALL = List.of(HTTP_STATUS_200, HTTP_STATUS_400, HTTP_STATUS_401, HTTP_STATUS_403, HTTP_STATUS_500, HTTP_STATUS_429);

    /**
     * Code
     */
    private final String responseCode;

    /**
     * Description
     */
    private final String description;
}
