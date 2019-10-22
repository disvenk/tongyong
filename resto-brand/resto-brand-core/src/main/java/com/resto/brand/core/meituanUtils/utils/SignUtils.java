package com.resto.brand.core.meituanUtils.utils;


import java.security.MessageDigest;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by nielongyu on 15/12/3.
 * Copied by cuibaosen on 16/8/4
 */
public final class SignUtils {
    /**
     * 根据secretKey和参数列表生成sign
     *
     * @param secret secretKey
     * @param params 参数列表
     */
    public static String createSign(String secret, Map<String, String> params) {
        // 自然排序
        Set<String> sortedParams = new TreeSet<String>();
        sortedParams.addAll(params.keySet());

        StringBuilder strB = new StringBuilder();
        // 排除sign和空值参数
        for (String key : sortedParams) {
            if (key.equalsIgnoreCase("sign")) {
                continue;
            }
            String value = params.get(key);
            if (value != null && !value.isEmpty()) {
                strB.append(key).append(value);
            }
        }

        String source = secret + strB.toString();
        return createSign(source);
    }

    /**
     * 生成新sign
     *
     * @param str 字符串
     * @return String
     */
    private static String createSign(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验请求响应sign
     *
     * @param secret secretKey
     * @param params 参数列表
     */
    public static boolean checkSign(String secret, Map<String, String> params) {
        return params.containsKey("sign") && params.get("sign").equals(SignUtils.createSign(secret, params));
    }
}