package com.resto.brand.core.util;


import java.io.File;

/**
 * Created by carl on 2017/8/2.
 */
public class FastdfsImageUtil {

    private static String IMAGE_SERVER_BASE_URL = "http://106.14.44.167/";

    public static void main(String[] args) {
        File picFile = new File("D:\\ag4sp1mBkYGAdIPeAAAOS6GSs2A319.png");
        try {
            String e = "";
            if(picFile.getAbsolutePath().length() >= 0) {
                e = picFile.getPath().substring(picFile.getPath().lastIndexOf(".") + 1);
            }

            FastDFSClient client = new FastDFSClient("classpath:client.properties");
            String path = picFile.getPath().replaceAll("/", "//");
            String url = client.uploadFile(path, e);
            url = IMAGE_SERVER_BASE_URL + url;
            System.out.println(url);
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

}
