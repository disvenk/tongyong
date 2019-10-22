package com.resto.api.brand.util;

import com.alipay.api.internal.util.StringUtils;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.resto.api.brand.util.HttpClient.doPost;

/**
 * Created by KONATA on 2017/2/3.
 * 用户行为日志记录的工具类
 */
public class UserActionUtils {

    private static final String path = "106.14.44.167";

    private static final String userName = "resto";

    private static final String passWord = "2017";

    private static final String tfpPath = "ftp://resto:2017@106.14.44.167/";

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    private UserActionUtils() {
    }

    /**
     * 往指定路径写入用户行为日志
     *
     * @param path       存放行为日志的路径
     * @param brandName  品牌名称
     * @param customerId 用户id
     * @param action     行为描述
     */
    public static boolean write(String path, String brandName, String customerId, String action) {
        PrintWriter printWriter = null;
        boolean result = false;
//        try {
//            //判断主路径是否存在
//            File basePackage = new File(path);
//            if (!basePackage.exists()) {
//                //如果没有则创建
//                basePackage.mkdir();
//            }
//            final String encoding = System.getProperty("file.encoding");
//            //在主路径下创建当日的路径
//
//
//            File datePackage = new File(path + "/" + simpleDateFormat.format(new Date()));
//            //如果没有则创建
//            if (!datePackage.exists()) {
//                datePackage.mkdir();
//            }
//
//            //在日期路径下创建品牌路径
//            brandName = new String(brandName.getBytes(), encoding);
//            File brandPackage = new File(datePackage + "/" + brandName);
//            if (!brandPackage.exists()) {
//                brandPackage.mkdir();
//            }
//
//
//            //在店铺路径下创建用户行为日志
//            File userLog = new File(brandPackage + "/" + customerId + ".txt");
//            if (!userLog.exists()) {
//                userLog.createNewFile();
//            }
//
//            printWriter = new PrintWriter(new FileWriter(userLog, true));
//            StringBuilder msg = new StringBuilder();
//            msg.append(sdf.format(new Date())).append("------").append(action);
//            printWriter.println(msg);
//            result = true;
//        } catch (IOException e) {
//            return result;
//        } finally {
//            if (printWriter != null) {
//                printWriter.close();
//            }
//        }
        return result;
    }

    public static void writeToFtp(String actionType, String brandName, String fileName, String action) {
//        Boolean has = createDir(actionType, brandName,null, fileName);
////
//        createFile(actionType, brandName,null, fileName, action, has);
    }

    public static void writeToFtp(String actionType, String brandName,String shopName, String fileName, String action) {
//        Boolean has = createDir(actionType, brandName,shopName, fileName);
////
//        createFile(actionType, brandName,shopName, fileName, action, has);
    }


    public static Boolean createDir(String actionType, String brandName,String shopName,String fileName) {
        FTPClient client = new FTPClient();
        PrintWriter printWriter = null;
        final String encoding = System.getProperty("file.encoding");

        try {
            client = FtpTool.getClient(path, userName, passWord);
            String currentPath = client.currentDirectory();
            String actionPath = currentPath + "/" + actionType;
            //创建行为类型文件夹
            if (!FtpTool.exists(client, actionType)) {
                FtpTool.mkdirs(client, actionType);
            }
            client.changeDirectory(actionPath);
            brandName = new String(brandName.getBytes(), encoding);
            String brandPath = actionPath + "/" + brandName;

            //创建品牌文件夹
            if (!FtpTool.exists(client, brandName)) {
                FtpTool.mkdirs(client, brandName);
            }
            client.changeDirectory(brandPath);
            String date = simpleDateFormat.format(new Date());
            String datePath = brandPath + "/" + date;

            if (!FtpTool.exists(client, date)) {
                FtpTool.mkdirs(client, date);
            }

            client.changeDirectory(datePath);

            if(!StringUtils.isEmpty(shopName)){
                shopName = new String(shopName.getBytes(), encoding);
                String shopPath = datePath + "/" + shopName;

                //创建品牌文件夹
                if (!FtpTool.exists(client, shopName)) {
                    FtpTool.mkdirs(client, shopName);
                }
                client.changeDirectory(shopPath);
            }



            if (!FtpTool.exists(client, fileName + ".txt")) {
                return false;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect(false);
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
            } catch (FTPException e) {
                e.printStackTrace();
            }
        }
        return true;
    }




    public static void createFile(String actionType, String brandName,String shopName,String fileName, String action, Boolean has) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String encoding = System.getProperty("file.encoding");
        URL base = null;
        try {
            brandName = new String(brandName.getBytes(), encoding);
            fileName = new String(fileName.getBytes(), encoding);
            String date = simpleDateFormat.format(new Date());
            StringBuilder content = new StringBuilder();
            if(StringUtils.isEmpty(shopName)){
                base = new URL(tfpPath + actionType + "/" + brandName + "/" + date + "/" + fileName + ".txt");
            }else{
                base = new URL(tfpPath + actionType + "/" + brandName + "/" + date + "/" +shopName + "/" + fileName + ".txt");
            }

            if (has) { //如果存在文件
                BufferedReader bufr = new BufferedReader(new InputStreamReader(base.openConnection().getInputStream()));
                String line;
                while (!StringUtils.isEmpty(line = bufr.readLine())) {
                    content.append(line).append("\n");
                }
                bufr.close();
            }
            content.append(sdf.format(new Date())).append("------").append(action);
            PrintWriter pw = new PrintWriter(base.openConnection().getOutputStream());
            pw.write(content.toString());
            pw.flush();
            pw.close();

        } catch (Exception e) {

        } finally {

        }
    }

//    public static void main(String[] args) {
//        writeToFtp("posAction", "老寅", "军师232", "母牛");
//    }

public static void main(String[] args) {
    Map map = new HashMap(4);
    map.put("brandName", "测试专用品牌");
    map.put("fileName", "2702e744571e466592d552e29d4fc59c");
    map.put("type", "UserAction");
    map.put("content", "系统向用户:"+"月半先生"+"推送微信消息:"+"测试数据"+",请求服务器地址为:" +"139.196.222.42");
    doPost(LogUtils.url, map);
}


}
