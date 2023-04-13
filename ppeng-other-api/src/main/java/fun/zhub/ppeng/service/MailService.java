package fun.zhub.ppeng.service;

/**
 * <p>
 * 发送邮件的接口类
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-08
 **/
public interface MailService {


    /**
     * 发送注册邮件
     *
     * @param mail mail
     */
    void sendRegisterEmail(String mail);


    /**
     * 发送修改信息的验证码邮件
     *
     * @param mail mail
     */
    void sendUpdateEmail(String mail);


    void sendEmailVerificationCode(String mailFrom, String toAddress, String verifyCode, String type);
}
