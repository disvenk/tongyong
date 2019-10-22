package com.resto.brand.core.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by disvenk.dai on 2018-06-15 10:35
 */
public class PdfCreateUtils {

    public static String getTicket(String serialNo,String platformTid,String payeeRegisterNo) {
        HttpClient client = new DefaultHttpClient();
        String ticket = FaPiaoUtils.getTicket(serialNo, platformTid, payeeRegisterNo);
        HttpGet get = new HttpGet(ticket);
        HttpResponse execute = null;
        try {
            execute = client.execute(get);
            InputStream inputStream = execute.getEntity().getContent();
            String path = new PdfCreateUtils().getClass().getResource("/").getPath();

            //获取item中的上传文件的输入流
            String s = UUID.randomUUID().toString();
            //创建一个文件输出流
            FileOutputStream out = new FileOutputStream(path + s + ".pdf");
            //创建一个缓冲区
            byte buffer[] = new byte[1024];
            //判断输入流中的数据是否已经读完的标识
            int len = 0;
            //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
            while ((len = inputStream.read(buffer)) > 0) {
                //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                out.write(buffer, 0, len);
            }
            //关闭输入流
            inputStream.close();
            //关闭输出流
            out.close();
            //删除处理文件上传时生成的临时文件

            String path1 = new PdfCreateUtils().getClass().getResource("/" + s + ".pdf").getPath();
            //boolean b = EmailUtil.sendEmail(new Email("smtp.163.com", "dd", "13317182430", "yanduo@24799", "yanzhuan051", "13477058114@163.com", "545279664@qq.com", "自动回复", "来自卷神的回复", new String[]{path1}));
            return path1;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
