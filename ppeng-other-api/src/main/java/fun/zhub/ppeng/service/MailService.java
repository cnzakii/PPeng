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


    /**
     * 发送模版邮件
     *
     * @param mailFrom     发件人邮箱
     * @param mailFromNick 发件人昵称
     * @param mailTo       收件人邮箱
     * @param subject      主题
     * @param type         操作类型：注册账号 or 修改账号
     * @param verifyCode   验证码
     */
    void sendEmailVerificationCode(String mailFrom, String mailFromNick, String mailTo, String subject, String type, String verifyCode);
}
