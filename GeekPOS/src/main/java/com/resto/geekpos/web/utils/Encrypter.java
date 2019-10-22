package com.resto.geekpos.web.utils;

import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class Encrypter {

    private static Logger logger = Logger.getLogger(Encrypter.class);

    private static Cipher ecipher;
    private static Cipher dcipher;
    private static final String key = "*:@7$8!t*:@7$8!t*:@7$8!t"; // 必须24个字符
    private static final String alg = "DESede";

    static {
        try {
            SecretKey skey = new SecretKeySpec(key.getBytes(), alg);
            ecipher = Cipher.getInstance(alg);
            dcipher = Cipher.getInstance(alg);
            ecipher.init(Cipher.ENCRYPT_MODE, skey);
            dcipher.init(Cipher.DECRYPT_MODE, skey);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
     * <pre>
     * 加密
     * </pre>
     *
     * @param str
     * @return
     */
    public static String encrypt(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        String encryptStr = "";
        try {
            byte[] utf8 = str.getBytes("UTF8"); // Encode the string into bytes using utf-8
            byte[] enc = ecipher.doFinal(utf8); // Encrypt
            encryptStr = Base64Support.toStr(enc); // Encode bytes to base64 to get a string
        } catch (Exception e) {
            logger.error("", e);
        }
        return encryptStr;
    }

    /**
     * <pre>
     * 解密
     * </pre>
     *
     * @param str
     * @return
     */
    public static String decrypt(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        String encryptStr = "";
        try {
            byte[] dec = Base64Support.fromStr(str); // Decode base64 to get bytes
            byte[] utf8 = dcipher.doFinal(dec); // Decrypt
            encryptStr = new String(utf8, "UTF8"); // Decode using utf-8
        } catch (Exception e) {
            logger.error("", e);
        }
        return encryptStr;
    }

    public static void main(String[] args) {
        System.out.println(encrypt("qiangde_test"));
        System.out.println(decrypt("SGBDFQuYtmhz11j4kUk94A.."));
        System.out.println(encrypt("12345678"));
        System.out.println(decrypt("siIz5BIH_rblnVElaHjwrw.."));
        System.out.println(encrypt("bczhao"));
        System.out.println(encrypt("zhao_360_Cai"));

        System.out.println(encrypt("bczhao"));
        System.out.println(encrypt("qicai123"));

        System.out.println(decrypt("siIz5BIH_rblnVElaHjwrw.."));
    }
}
