package com.resto.brand.core.entity.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;


import java.util.Date;
import java.util.Properties;

/**
 * 邮件引擎

 */
public final class EmailSender {
    private final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private MimeMessage mimeMsg; // MIME邮件对象
    private Session session; // 邮件会话对象
    private Properties props; // 系统属性

    private String username = ""; // smtp认证用户名和密码
    private String password = "";
    private String userkey = "";

    private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

    public EmailSender(String smtp) {
        try {
            setSmtpHost(smtp);
            createMimeMessage();
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    /**
     * 设置SMTP主机
     * 例如：smtp.163.com
     *
     * @param hostName String
     */
    public void setSmtpHost(String hostName) {
//		if (props == null)
//			props = System.getProperties(); // 获得系统属性对象
//		props.put("mail.smtp.host", hostName); // 设置SMTP主机
        // props.put("mail.smtp.port", "995");
        // props.put("")

        if(props==null){
            props = new Properties();
            props.put("mail.smtp.host",hostName);
            props.put("mail.debug", "true");//便于调试
        }
    }

    /**
     * 创建MIME邮件对象
     *
     * @return boolean
     */
    public boolean createMimeMessage() {
        try {
            session = Session.getDefaultInstance(props, null); // 获得邮件会话对象
        } catch (Exception e) {
            return false;
        }
        try {
            mimeMsg = new MimeMessage(session); // 创建MIME邮件对象
            mp = new MimeMultipart();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param name String
     * @param pass String
     *  key 授权码
     * 判断发送方邮箱 密码是否正确
     */
    public void setNamePass(String name, String pass, String key) {
        username = name;
        password = pass;
        userkey = key;
        setNeedAuth();
    }


    /**
     * 判断是否需要授权码
     */
    private void setNeedAuth() {
        if (props == null)
            props = System.getProperties();
        if (userkey == null || userkey.trim().equals("")) {
            props.put("mail.smtp.auth", "false");
        } else {
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable","true");
        }
    }


    /**
     * 设置内容
     *
     * @param mailBody String
     */
    public boolean setBody(String mailBody) {
        try {
            BodyPart bp = new MimeBodyPart();
            bp.setContent("" + mailBody, "text/html;charset=UTF-8");
            mp.addBodyPart(bp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置附件
     *
     */
    public boolean addFileAffix(String filename) {
        try {
            BodyPart bp = new MimeBodyPart();
            FileDataSource fileds = new FileDataSource(filename);
            bp.setDataHandler(new DataHandler(fileds));
            bp.setFileName(MimeUtility.encodeText(fileds.getName()));
            mp.addBodyPart(bp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(filename, e);
            return false;
        }
    }

    /**
     * 设置发信人
     */
    public boolean setFrom(String from) {
        try {
            String[] f = from.split(",");
            if (f.length > 1) {
                from = MimeUtility.encodeText(f[0]) + "<" + f[1] + ">";
            }
            mimeMsg.setFrom(new InternetAddress(from)); // 设置发信人
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置主题
     *
     * @param mailSubject String
     * @return boolean
     */
    public boolean setSubject(String mailSubject) {
        try {
            mimeMsg.setSubject(mailSubject, "UTF-8");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置收信人
     *
     */
    public boolean setTo(String to) {
        if (to == null)
            return false;
        try {
            mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置抄送人
     */
    public boolean setCopyTo(String copyto) {
        if (copyto == null)
            return false;
        try {
            mimeMsg.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress.parse(copyto));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送邮件
     *
     */
    public boolean sendout() {
        try {
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();

            Session mailSession = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    if (userkey == null || "".equals(userkey.trim())) {
                        return null;
                    }
                    return new PasswordAuthentication(username, userkey);
                }
            });
            Transport transport = mailSession.getTransport("smtp");
            transport.connect((String) props.get("mail.smtp.host"), username, password);
            // 设置发送日期
            mimeMsg.setSentDate(new Date());
            // 发送
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
            if (mimeMsg.getRecipients(Message.RecipientType.CC) != null) {
                transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
            }
            transport.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

