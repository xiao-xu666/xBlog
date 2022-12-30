package com.xiaoxu.xBlog.Controller;

import com.xiaoxu.xBlog.Entities.UserInfo;
import com.xiaoxu.xBlog.Utils.ReturnResults;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;


@RestController
@Slf4j
public class EmailController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/sendEmail")
    public ReturnResults sendMsg(String registerEmail,String content){
        // 发送邮件
        // 收件人电子邮箱
        String to = registerEmail;

        if (registerEmail == null) {
            //获取缓存中校验的用户信息
            UserInfo resetUser = (UserInfo) redisTemplate.opsForValue().get("resetUser");
            //删除缓存信息
            redisTemplate.delete("resetUser");
            String userEmail = resetUser.getUserEmail();
            to = userEmail;
        }


        MimeMessage message = mailSender.createMimeMessage();
        // true代表是multipart类型
        log.info("开始发送邮件");
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            //发送者
            helper.setFrom("2529023117@qq.com");
            //接收者
            helper.setTo(to);
            //邮件主题
            helper.setSubject("xBlog博客邮箱验证");
            //邮件内容
            helper.setText(content, true);
            mailSender.send(message);
            log.info("邮件发送成功");
            return ReturnResults.success("发送成功，请注意接收");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ReturnResults.error("发送失败");
        }
    }
}
