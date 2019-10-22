package com.resto.brand.core.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URL;


/**
 * Created by KONATA on 2017/2/14.
 */
public class FtpUtils {

    public static void upload(String src,String fileName,byte [] bytes) throws IOException {
        URL url = new URL(src + fileName);
        BufferedOutputStream outputStream = new BufferedOutputStream(url.openConnection().getOutputStream());
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }




}
