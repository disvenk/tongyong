package com.resto.brand.core.util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KONATA on 2017/3/13.
 */
public class LogUtils implements Runnable {


    private static final String POS_LOG_URL = "/logAction/";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //    public static final String url = "http://10.25.23.60:8580/pos/posAction";
//     public static final String url = "http://139.196.222.182:8580/pos/posAction";

    private String brandName;

    private String fileName;

    private String content;

    private String type;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LogUtils(String brandName, String fileName, String content, String type) {
        this.brandName = brandName;
        this.fileName = fileName;
        this.content = content;
        this.type = type;
    }


    public LogUtils() {
    }

    @Override
    public void run() {
        action();
    }


    private synchronized void action() {
        PrintWriter printWriter = null;
        try {
            //创建当日文件夹
            final String encoding = System.getProperty("file.encoding");
            brandName = new String(brandName.getBytes(), encoding);
            fileName = new String(fileName.getBytes(), encoding);
            String day = sdf.format(new Date());
            File dayDir = new File(POS_LOG_URL + type + "/" + day);
            if (!dayDir.exists()) {
                dayDir.mkdirs();
            }
            //创建品牌文件夹
            File brandDir = new File(POS_LOG_URL + type + "/" + day + "/" + brandName);
            if (!brandDir.exists()) {
                brandDir.mkdir();
            }

            File txt = null;
            txt = new File(POS_LOG_URL + type + "/" + day + "/" + brandName + "/" + fileName + ".txt");

            if (txt != null && !txt.exists()) {
                txt.createNewFile();
            }


            printWriter = new PrintWriter(new FileWriter(txt, true));
            StringBuilder msg = new StringBuilder();
            msg.append(simpleDateFormat.format(new Date())).append("------").append(content);
            printWriter.println(msg.toString());
            printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }

    }


    public static void writeLog(String brandName, String fileName, String content, String type) {
        LogUtils logUtils = new LogUtils(brandName, fileName, content, type);
        Thread thread = new Thread(logUtils);
        thread.setDaemon(true);
        thread.start();
    }


}
