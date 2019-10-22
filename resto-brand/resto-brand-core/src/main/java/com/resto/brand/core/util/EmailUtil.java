package com.resto.brand.core.util;


import com.resto.brand.core.entity.email.Email;
import com.resto.brand.core.entity.email.EmailSender;

/**
 * 发送邮件辅助类
 *
 */
public final class EmailUtil {
    private EmailUtil() {
    }

    /**
     * 发送邮件
     */
    public static final boolean sendEmail(Email email) {
        // 初始化邮件引擎
        EmailSender sender = new EmailSender(email.getHost());
        sender.setNamePass(email.getName(), email.getPassword(), email.getKey());
        if (sender.setFrom(email.getFrom()) == false)
            return false;
        if (sender.setTo(email.getSendTo()) == false)
            return false;
        if (email.getCopyTo() != null && sender.setCopyTo(email.getCopyTo()) == false)
            return false;
        if (sender.setSubject(email.getTopic()) == false)
            return false;
        if (sender.setBody(email.getBody()) == false)
            return false;
        if (email.getFileAffix() != null) {
            for (int i = 0; i < email.getFileAffix().length; i++) {
                if (sender.addFileAffix(email.getFileAffix()[i]) == false)
                    return false;
            }
        }
        // 发送


        return sender.sendout();
    }

    public static void main(String[] args) {
        /**
         * @param host 服务器地址
         * @param from 发送人
         * @param name 登录名
         * @param password 登录密码
         * @param key 授权码
         * @param sendTo 接收人
         * @param copyTo 抄送人
         * @param topic 主题
         * @param body 内容
         */


        //测试发送邮件
        sendEmail(new Email("smtp.qq.com", "545279664@qq.com", "545279664@qq.comm", "", "", "13477058114@163.com", "", "自动回复", "来自卷神的回复", new String[]{"D:\\text.pdf"}));
    }
    //yevnhpsrzovobeai
}