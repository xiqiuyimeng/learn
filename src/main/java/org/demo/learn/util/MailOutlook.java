package org.demo.learn.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;

/**
 * @author luwt-a
 * @date 2023/10/8
 */
public class MailOutlook {

    static {
        System.setProperty("mail.mime.splitlongparameters", "false");
        System.setProperty("mail.mime.charset", "UTF-8");
    }


    public static void sendEmail() {
        try {
            String fromEmail = "dhr@notice.glodon.com"; // 发送邮箱
            String toEmail = "luwt-a@glodon.com,liyd-b@glodon.com"; // 接收邮箱
            String password = "UhFPC2pVAUkb";//发送人的邮箱密码
            Session session = initProperties(fromEmail, password, "smtp", "activate.corpemail.net", "587", false);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));

            message.addRecipients(Message.RecipientType.TO, toEmail);
            message.setSubject("测试约会"); // 会议标题
            StringBuffer buffer = new StringBuffer();
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // 数据库的时间格式
            DateFormat format2 = new SimpleDateFormat("yyyyMMdd:HHmmss"); // 发送到outlook的时间格式
            // 设置时区
            format2.setTimeZone(TimeZone.getTimeZone("GMT"));
            String beginTime = format2.format(format1.parse("2023-10-10 15:00")); // 会议开始时间
            beginTime = beginTime.replace(":", "T");
            String endTime = format2.format(format1.parse("2023-10-10 16:00")); // 会议结束时间
            endTime = endTime.replace(":", "T");
            String uuid = UUID.randomUUID().toString(); // 如果id相同的话，outlook会认为是同一个会议请求，所以使用uuid。 如果传同一ID不同内容的会议会修改上一个会议
            System.out.println("UUID:" + uuid);
            buffer.append("BEGIN:VCALENDAR\n"
                    + "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n"
                    + "VERSION:2.0\n"
                    + "METHOD:REQUEST\n"
                    + "BEGIN:VEVENT\n"
                    + "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"+toEmail+"\n"
                    + "ORGANIZER:MAILTO:"+toEmail+"\n"
                    + "DTSTART:"
                    + beginTime+"\n"
                    + "DTEND:"
                    + endTime+"\n"
                    + "UID:"+uuid+"\n"
                    + "CATEGORIES:"+"邮件类别111"+"\n"  // 类别
                    + "DESCRIPTION:"+"会议地点222"+"\n\n"  // 地点
                    + "SUMMARY:Test meeting request\n" + "PRIORITY:5\n"
                    + "CLASS:PUBLIC\n" + "BEGIN:VALARM\n"
                    + "TRIGGER:-PT15M\n" + "ACTION:DISPLAY\n"
                    + "DESCRIPTION:Reminder\n" + "END:VALARM\n"
                    + "END:VEVENT\n" + "END:VCALENDAR");
            BodyPart messageBodyPart = new MimeBodyPart();
            // 测试下来如果不这么转换的话，会以纯文本的形式发送过去，
            // 如果没有method=REQUEST;charset=\"UTF-8\"，outlook会议附件的形式存在，而不是直接打开就是一个会议请求
            messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(buffer.toString(),
                    "text/calendar;method=REQUEST;charset=\"UTF-8\"")));
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Session initProperties(String account, String password, String protocol, String host, String port, boolean enableSsl) {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory mailSslSocketFactory = null;
        try {
            mailSslSocketFactory = new MailSSLSocketFactory();
            mailSslSocketFactory.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        properties.put("mail.smtp.enable", "true");

        if (enableSsl) {
            properties.put("mail.smtp.ssl.socketFactory", mailSslSocketFactory);
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.ssl.checkserveridentity", true);
            properties.put("mail.smtp.socketFactory.fallback", "false");
            properties.put("mail.smtp.socketFactory.port", port);
        }

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account, password);
            }
        });
        session.setDebug(true);
        return session;
    }


    public static void main(String[] args){
        sendEmail();
        System.out.println("success");

    }
}
