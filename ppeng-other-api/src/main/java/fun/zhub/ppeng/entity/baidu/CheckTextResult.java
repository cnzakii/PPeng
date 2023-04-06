package fun.zhub.ppeng.entity.baidu;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 百度文本审核结果封装类
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-06
 **/

@Data
public class CheckTextResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    // 请求唯一id
    private Long log_id;

    // 错误提示码，失败才返回，成功不返回
    private Long error_code;

    // 错误提示信息，失败才返回，成功不返回
    private String error_msg;

    // 审核结果，可取值：合规、不合规、疑似、审核失败
    private String conclusion;

    // 审核结果类型，可取值1.合规，2.不合规，3.疑似，4.审核失败
    private Integer conclusionType;

    // 不合规/疑似/命中白名单项详细信息。
    private Result[] data;

}
