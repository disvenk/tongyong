package com.resto.shop.web.util.encrypt;

import java.security.MessageDigest;

/**
 * Created by zhaojingyang on 2015/10/30.
 */

public class MD5Util {
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String MD5Encode(String origin, String charsetName) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
            }
        } catch (Exception exception) {

        }
        return resultString;
    }

    public static byte[] MD5(String origin, String charsetName) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName)) {
                return md.digest(origin.getBytes());
            } else {
                return md.digest(origin.getBytes(charsetName));
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


    public static void main(String args[]) {
        String ss = "{'id':1,'skuName':'爱妃苹果'}";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] mds = md.digest(ss.getBytes());
            for (byte b : mds) {
                System.out.print(b);
            }
            System.out.println();
            System.out.println(byteArrayToHexString(mds));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String ds = MD5Util.MD5Encode(ss, "utf-8");
            System.out.println(ds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
