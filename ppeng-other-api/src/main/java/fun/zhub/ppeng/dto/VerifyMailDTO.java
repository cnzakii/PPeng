package fun.zhub.ppeng.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 邮件发送模版
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyMailDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 发件人邮箱
     */
    private String mailFrom;


    /**
     * 发件人邮箱
     */
    private String mailFromNick;

    /**
     * 收件人邮箱
     */
    private String mailTo;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 操作类型：注册账号 or 修改账号
     */
    private String type;

    /**
     * 验证码
     */
    private String verifyCode;

}
