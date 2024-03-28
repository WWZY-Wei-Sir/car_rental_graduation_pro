package wsir.carrental.controller;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wsir.carrental.service.MailService;
import wsir.carrental.util.RedisUtil;
import wsir.carrental.util.Result;

import java.net.HttpURLConnection;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;

    @Autowired
    private RedisUtil redisUtil;

    // sendVerificationCode发送验证码
    @GetMapping("/sendPassCode")
    public <T> Result<T> sendPassCode(@RequestParam String mailbox) {
        if (StrUtil.isBlank(mailbox)) {
            return Result.error(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "请求的邮箱为空!");
        }
        if (null != redisUtil.get(mailbox)) {
            return Result.error(HttpURLConnection.HTTP_CONFLICT, "发送到该邮箱的验证码仍有效!");
        }
        mailService.sendPassCode(mailbox);
        return Result.success();
    }
}
