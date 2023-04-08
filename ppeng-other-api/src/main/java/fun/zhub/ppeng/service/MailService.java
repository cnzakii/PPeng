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
     * 发送简单文字邮件
     *
     * @param mailFrom     发件人邮箱
     * @param mailFromNick 发件人昵称
     * @param mailTo       收件人邮箱
     * @param cc           抄送人邮箱(可为空，方法内部处理)
     * @param subject      主题
     * @param content      内容
     */
    void sendSimpleMail(String mailFrom, String mailFromNick, String mailTo, String cc, String subject, String content);
}
