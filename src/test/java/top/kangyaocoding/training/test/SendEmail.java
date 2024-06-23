package top.kangyaocoding.training.test;
 
import com.sun.mail.util.MailSSLSocketFactory;
 
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
 
public class SendEmail implements Runnable{
    private String email;// 收件人邮箱
    private String userName;// 用户名
    private Long id; // 用户id
    private String detail; //留言内容
    private int type; //留言/回复
    private String pageUrl; //页面路径
    private String articleTitle; //文章标题
 
 
    public SendEmail(String email, String detail,  String userName, int type, String pageUrl, String articleTitle) {
        this.email = email;
        this.userName = userName;
        this.detail=detail;
        this.type = type;
        this.pageUrl=pageUrl;
        this.articleTitle=articleTitle;
    }
 
    public void run() {
        // 1.创建连接对象javax.mail.Session
        // 2.创建邮件对象 javax.mail.Message
        // 3.发送一封激活邮件
        String from = "kaiouken@mail.kangyaocoding.top";// 发件人电子邮箱
        String host = "smtpdm.aliyun.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)
        String code = "fyuKangYaotpwbebh01"; // 发件人邮箱账号、授权码
 
        Properties properties = System.getProperties();// 获取系统属性
 
        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        properties.setProperty("mail.transport.protocol","smtp");
      //  properties.setProperty("mail.smtp.port", "587");  //不需要 默认端口
        properties.setProperty("mail.smtp.auth", "true");// 打开认证
      //  properties.put("mail.smtp.socketFactory", "javax.net.ssl.SSLSocketFactory");
 
        try {
            // QQ邮箱需要下面这段代码，163邮箱不需要
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2"); //加上这句解决问题
 
            // 1.获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, code);
                }
            });
 
            session.setDebug(true);
 
            //2、通过session得到transport对象
            Transport transport = session.getTransport();
            //3、使用用户名和授权码连上邮件服务器
            transport.connect(host,from,code);
 
 
            // 4.创建邮件对象
            Message message = new MimeMessage(session);
            // 4.1设置发件人
            message.setFrom(new InternetAddress(from));
            // 4.2设置接收人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 4.3设置邮件主题
            String title = "测试邮件";
            if(type==0){
                title = "亲爱的主人，BLOG有新留言了";
            }else if(type ==1){
                title = "ZJBLOG留言回复通知";
            }else if(type ==2){
                title = "亲爱的主人，ZJBLOG有新评论了";
            }else if(type ==3){
                title = "ZJBLOG评论回复通知";
            }
            message.setSubject(title);
            // 2.4设置邮件内容
            String content = "";
            if(type==0){
                content = "<html><head></head><body>测试1</body></html>";
            }else if(type==3){
                content = "<html><head></head><body>测试2</body></html>";
            }
            message.setContent(content, "text/html;charset=UTF-8");
 
 
            //5、发送邮件
            transport.sendMessage(message,message.getAllRecipients());
            //6、关闭连接
            transport.close();
            System.out.println("邮件成功发送!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new SendEmail("1022291728@qq.com", "测试邮件内容",
                "kaiouken",0,"","")).start();
    }
}