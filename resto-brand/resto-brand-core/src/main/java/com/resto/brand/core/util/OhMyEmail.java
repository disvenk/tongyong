package com.resto.brand.core.util;

import com.resto.brand.core.Constant.EmailConstant;
import javax.mail.MessagingException;

import java.io.File;

import static io.github.biezhi.ome.OhMyEmail.SMTP_163;
import static io.github.biezhi.ome.OhMyEmail.SMTP_ENT_QQ;
import static io.github.biezhi.ome.OhMyEmail.SMTP_QQ;

/**
 * Created by disvenk.dai on 2018-06-14 16:48
 */
public class OhMyEmail {
    /**
     * @param subject 主题
     * @param from    发件人别名
     * @param to      收件人
     * @param text    文本信息
     * @param html    模板信息
     */
    public static boolean sendEmail(String email,String key,String subject, String from, String to, String text, String html,File file) {
        if (email.split("@")[1].equals("163.com")){
            io.github.biezhi.ome.OhMyEmail.config(SMTP_163(false), email, key);
        }else {
            io.github.biezhi.ome.OhMyEmail.config(SMTP_ENT_QQ(false), email, key);
        }
        try {
            io.github.biezhi.ome.OhMyEmail.subject(subject)
                    .from(from)
                    .to(to)
                    .text(text)
                    .html(html)
                    .attach(file)
                    .send();
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            //throw new RuntimeException("发送邮件失败");
            return false;
        }

    }

    public static void main(String[] args) {
        String subject = "豆捞坊配置上线";

        String html = "<table>\n" +
                "    <tr><td>配置状态：</td><td>已完成配置</td></tr>\n" +
                "    <tr><td>品牌名称：</td><td>豆捞坊</td></tr>\n" +
                "    <tr><td>门店名称：</td><td>豆捞坊金桥店</td></tr>\n" +
                "  </table>\n" +
                "  <table border=\"1\" style=\"border-collapse:collapse;margin-top: 30px;\">\n" +
                "      <tr>\n" +
                "        <th rowspan=\"2\">门店名称</th>\n" +
                "        <th rowspan=\"2\">计费方式</th>\n" +
                "        <th rowspan=\"2\">新用户消费订单</th>\n" +
                "        <th colspan=\"2\">新用户消费订单</th>\n" +
                "        <th colspan=\"2\">回头用户消费订单</th>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <th>抽佣对象</th>\n" +
                "        <th>抽佣比例</th>\n" +
                "        <th>抽佣对象</th>\n" +
                "        <th>抽佣比例</th>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td>大宁店</td>\n" +
                "        <td>按效果收费</td>\n" +
                "        <td>订单总额</td>\n" +
                "        <td>10</td>\n" +
                "        <td>实收金额</td>\n" +
                "        <td>12</td>\n" +
                "        <td>100</td>\n" +
                "      </tr>\n" +
                "  </table>\n" +
                "  <table border=\"1\"  style=\"border-collapse:collapse;margin-top: 30px;\">\n" +
                "    <tr>\n" +
                "      <th>门店名称</th>\n" +
                "      <th>计费方式</th>\n" +
                "      <th>有效期（天）</th>\n" +
                "      <th>收取费用（元）</th>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td>金桥店</td>\n" +
                "      <td>按周期付费</td>\n" +
                "      <td>120</td>\n" +
                "      <td>1200</td>\n" +
                "    </tr>\n" +
                "  </table>";
        sendEmail("545279664@qq.com","",subject, "545279664@qq.com", "13477058114@163.com", "申请上线", "<a href=\"http://726637c6.ngrok.io/fapiao/fapiao\">下载发票</a>",null);
    }
}
