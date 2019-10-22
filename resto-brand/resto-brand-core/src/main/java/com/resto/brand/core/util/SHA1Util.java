package com.resto.brand.core.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 将支付通过rsa1加密
 */
public class SHA1Util {

    /**
     * 加密字符，按字典排序
     * @param strMap
     * @return
     */
    public static String meiTuanEncryptionByMap(Map<String, String> strMap) {
        String ciphertext = null;
        try {
            //key集合
            List<String> keyList = new ArrayList<>();
            for (String key : strMap.keySet()) {
                if (!"signKey".equalsIgnoreCase(key)) {
                    keyList.add(key);
                }
            }
            String[] kayArr = new String[keyList.size()];
            kayArr = keyList.toArray(kayArr);
            Arrays.sort(kayArr);
            StringBuffer str = new StringBuffer(strMap.get("signKey"));
            for (int i = 0; i < kayArr.length; i++) {
                str.append(kayArr[i]).append(strMap.get(kayArr[i]));
            }
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(str.toString().getBytes("UTF-8"));
            ciphertext = byteToStr(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ciphertext.toLowerCase();
    }


    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param digest
     * @return
     */
    private static String byteToStr(byte[] digest) {
        String strDigest = "";
        for (int i = 0; i < digest.length; i++) {
            strDigest += byteToHexStr(digest[i]);
        }
        return strDigest;
    }


    /**
     * 将字节转换为十六进制字符串
     *
     * @param b
     * @return
     */
    private static String byteToHexStr(byte b) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(b >>> 4) & 0X0F];
        tempArr[1] = Digit[b & 0X0F];
        String s = new String(tempArr);
        return s;
    }
}
