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
     * 发送更新用户密码的邮件
     *
     * @param userEmail email
     */
    void sendUpdatePasswordEmail(String userEmail);

    /**
     * 发送更新用户邮件的邮件
     *
     * @param userEmail email
     */
    void sendUpdateEmailEmail(String userEmail);

    /**
     * 发送删除用户的邮件
     *
     * @param userEmail email
     */
    void sendDeleteUserEmail(String userEmail);


    /**
     * 发送关于用户的邮件
     *
     * @param userEmail    用户邮件
     * @param redisKey     redis的key前缀
     * @param emailSubject 邮件主题
     * @param emailType    邮件类型
     */
    void sendEmailAboutUser(String userEmail, String redisKey, String emailSubject, String emailType);


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
