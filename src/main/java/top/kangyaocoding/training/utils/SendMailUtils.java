package top.kangyaocoding.training.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;

@Component
public class SendMailUtils {

    @Value("${spring.mail.username}")
    private String setFrom;

    private final String setFromName = "Kang Yao Coding";

    private final JavaMailSender javaMailSender;

    @Autowired
    public SendMailUtils(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // 发送普通文字邮件
    public void sendText(String subject, String text, String setTo) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject); // 标题
        simpleMailMessage.setText(text); // 内容
        simpleMailMessage.setFrom(String.format("%s <%s>", setFromName, setFrom)); // 发送人邮箱
        simpleMailMessage.setTo(setTo); // 发送目的地邮箱
        javaMailSender.send(simpleMailMessage);
    }

    // 发送带页面格式加文件邮件
    public void sendTextsWithAttachment(String subject, String text, Boolean isHtml, String setTo,
                                        String attachmentFilename, String filePathName) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(subject); // 标题
        helper.setText(text, isHtml); // 内容
        helper.setFrom(setFrom, setFromName); // 发送人邮箱
        helper.setTo(setTo); // 目的地邮箱
        helper.addAttachment(attachmentFilename, new File(filePathName)); // 附件
        javaMailSender.send(mimeMessage);
    }

    // 发送HTML格式邮件
    public void sendHtmlWithCode(String subject, String text, String code, String setTo) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        // 创建HTML内容
        String htmlContent = String.format(
                "<html><body>%s<br><strong style=\"font-size: 24px;\">%s</strong><br>动态验证码有效期为 3 分钟。<br>如果你没有尝试验证你的电子邮件地址，请忽略此电子邮件。<br><br>如有任何疑问，请<a href=\"mailto:kaiouken@foxmail.com\">点击这里</a>发送邮件联系我们。</body></html>",
                text, code
        );

        htmlContent = "<html><body><strong style=\"font-size: 36px;\">我是王者</strong><br></body></html>";

        helper.setSubject(subject); // 标题
        helper.setText(htmlContent, true); // HTML内容
        helper.setFrom(setFrom, "压缩包"); // 发送人邮箱
        helper.setTo(setTo); // 目的地邮箱

        javaMailSender.send(mimeMessage);
    }
}

