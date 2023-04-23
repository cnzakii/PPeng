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

    HTTP_STATUS_200("200", "ok"), // 请求成功
    HTTP_STATUS_400("400", "request error"), // 请求错误
    HTTP_STATUS_401("401", "no authentication"), // 没有认证(token)
    HTTP_STATUS_403("403", "no authorities"), // 没有权限
    HTTP_STATUS_500("500", "server error"); // 服务器内部错误

    public static final List<ResponseStatus> HTTP_STATUS_ALL = List.of(HTTP_STATUS_200, HTTP_STATUS_400, HTTP_STATUS_401, HTTP_STATUS_403, HTTP_STATUS_500);

    /**
     * Code
     */
    private final String responseCode;

    /**
     * Description
     */
    private final String description;
}
