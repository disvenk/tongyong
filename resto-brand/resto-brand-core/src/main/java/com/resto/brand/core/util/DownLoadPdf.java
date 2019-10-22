package com.resto.brand.core.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by disvenk.dai on 2018-06-14 15:51
 */
public class DownLoadPdf {

    public static void DownLoad(HttpServletResponse response,
                                HttpServletRequest request,
                                String serialNo,
                                String platformTid,
                                String payeeRegisterNo){
        HttpClient client = new DefaultHttpClient();
        String ticket = FaPiaoUtils.getTicket(serialNo, platformTid, payeeRegisterNo);
        HttpGet get = new HttpGet(ticket);
        HttpResponse execute = null;
        try {
            execute = client.execute(get);
            InputStream inputStream =  execute.getEntity().getContent();
            ServletOutputStream outputStream =response.getOutputStream();

            // byte[] bytes = new byte[1024*1024];
            //inputStream.read(bytes);
            response.setContentType("application/pdf");

            String filename = UUID.randomUUID().toString()+".pdf";
            String agent = request
                    .getHeader("user-agent");
            filename = FileUtils.encodeDownloadFilename(filename, agent);
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + filename);
            response.addHeader("Content-Length", "(37509");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for(int i;(i=inputStream.read())!=-1;){
                baos.write(i);
            }
            baos.flush();
            Document doc = new Document();
            doc.open();
            PdfStream pdfStream=new PdfStream(baos.toByteArray());
            PdfWriter pw = null;
            try {
                pw = PdfWriter.getInstance(doc,outputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            pdfStream.toPdf(pw,outputStream);
            pw.flush();

            baos.close();
            pw.close();
            outputStream.close();
            inputStream.close();
            //   doc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
