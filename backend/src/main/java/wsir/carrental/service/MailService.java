package wsir.carrental.service;

public interface MailService {
    // 发送验证码
    void sendPassCode(String mailbox);

    void sendCompanyLicense(String mailbox);

    // 判断验证码是否合理
    Integer checkPassCode(String mailbox, String passCode);
}
