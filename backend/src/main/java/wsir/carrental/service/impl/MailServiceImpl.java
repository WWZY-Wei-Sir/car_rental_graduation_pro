package wsir.carrental.service.impl;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import wsir.carrental.exception.ServiceException;
import wsir.carrental.service.MailService;
import wsir.carrental.util.RedisUtil;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.net.HttpURLConnection;
import java.util.Date;

@Service
public class MailServiceImpl implements MailService {
    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void sendPassCode(String mailbox) {
        SimpleMailMessage message = new SimpleMailMessage();
        String passCode = RandomUtil.randomNumbers(6);
        message.setFrom("汽车租赁系统<" + from + ">");
        message.setTo(mailbox);
        message.setSubject("汽车租赁系统验证码");
        message.setText("尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为:" + passCode + ",本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");    //设置邮件正文
        message.setSentDate(new Date());
        redisUtil.set(mailbox, passCode, 5 * 60);
        javaMailSender.send(message);
    }

    @Override
    public void sendCompanyLicense(String mailbox) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom("汽车租赁系统<" + from + ">");
            messageHelper.setTo(mailbox);
            messageHelper.setSubject("汽车租赁系统取车验证邮件");
            messageHelper.setText("尊敬的用户,您好:\n"
                    + "\n该封邮件附件包含本公司的证照基本信息，请在取车时出示该邮件给员工进行验证。");    //设置邮件正文
            messageHelper.setSentDate(new Date());
            File file = new File("E:\\WsirExclusive\\_DownloadFiles\\2023-04-30-08-43-03_46a45edd1ff24a0ea19bba3102f5bc31.pdf");
            messageHelper.addAttachment(file.getName(), file);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new ServiceException(HttpURLConnection.HTTP_INTERNAL_ERROR, "邮件发送失败");
        }
    }

    //0为不存在验证码，1为验证码不对，2为成功
    @Override
    public Integer checkPassCode(String mailbox, String passCode) {
        if (null == redisUtil.get(mailbox)) {
            return 0;
        }
        if (!redisUtil.get(mailbox).equals(passCode)) {
            return 1;
        }
        return 2;
    }
}
