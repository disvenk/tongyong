package com.resto.brand.core.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAEncryptionUtil {

//    private static final KeyPair keyPair = initKey();
//
//    private static KeyPair initKey(){
//        try {
//            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//            SecureRandom random = new SecureRandom();
//            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
//            generator.initialize(1024, random);
//            return generator.generateKeyPair();
//        }catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 生成public key
//     * @return
//     */
//    public static void generateBase64PublicKey(){
//        RSAPublicKey key = (RSAPublicKey)keyPair.getPublic();
//        System.out.println(new String(Base64.encodeBase64(key.getEncoded())));
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        System.out.println(new String(Base64.encodeBase64(privateKey.getEncoded())));
//    }
//

    //加密公钥
    private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFYCVeQqH9GNTvErE1HornwVXkADp+rfOy2lptELvuGAZkrmt9NtyU5nm3/RBLljhJuzlVwQwvI3wPVmE+Vnlc16ZlpyT8taudvfPU/HlzinA7bUcU7jauPCInlp7ztfKD3I0N/QS/ASZtEkvgbbKgxPerlVMstGzpv4NQ66mMjwIDAQAB";

    //加密私钥
    private static final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMVgJV5Cof0Y1O8SsTUeiufBVeQAOn6t87LaWm0Qu+4YBmSua3023JTmebf9EEuWOEm7OVXBDC8jfA9WYT5WeVzXpmWnJPy1q52989T8eXOKcDttRxTuNq48IieWnvO18oPcjQ39BL8BJm0SS+BtsqDE96uVUyy0bOm/g1DrqYyPAgMBAAECgYAUvvDekgFl0XzcLipLK40tI/CruOxtEUqqs3HKwfYG5iaR4ZVxwVj5G6qSkLpsP3tmIJQ7s8FcmQ26rPkmrL8vuEH5K3QDaGzRO29auzchOsGJ5z/Tt8qVgh7yEhlVr751362BgVtUSxlICNmmuu3jxT4yrd8bFSIb4WjDGOVIpQJBAOfzc7q8jAM+pdk8a1ivVTeQkHrA85LB5s3dEhwhsnjSDlN+/wf/lEvyFPQkQDqumE435ArI8M5mRTSyI66JE6MCQQDZ1vpE9iY+Dwgy4uy/6KZ1HBur+rHJ0flCXO+IcpjPtlpwKPB4O07tHS+8ISNFhMc39nfg/XEzVlbQeX1NKtIlAkEAzgTczAXgTpl8caFKr3C8Pig3S/DJ55Y2TK+JAFelz86zTtJD73TA4VvZ5Ke2LOka+o6GDFZCHPGVkqKPMt0qBQJAKQRlk16VzNGfxZUc5vWzghGM1FEb8NhsiqZdI+AQDexUExJiVQE110fLmSzUK17Y9BygbejKy9PR/OcjRMhvUQJAV5/4zk7phUAn4ldi4nTRpntqgFZhi9mkqzehT21iXK1VDFZhukv0nNOlxywnkeGojiFvOIARaficraomwgkbhg==";


    /**
     * 通过公钥byte[]将公钥还原，适用于RSA算法
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PublicKey getPublicKey() throws NoSuchAlgorithmException,InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }


    /**
     * 通过私钥byte[]将公钥还原，适用于RSA算法
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PrivateKey getPrivateKey() throws NoSuchAlgorithmException,InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }


    /**
     * 解密
     * @param string
     * @return
     */
    public static String decryptBase64(String string) {
        return new String(decrypt(Base64.decodeBase64(string)));
    }

    private static byte[] decrypt(byte[] string) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
            byte[] plainText = cipher.doFinal(string);
            return plainText;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception{
        // 解密
//        System.out.println(decryptBase64("uW49RoxZRo26MNNUvPvVt/mAkUw0dslW/WXxy0g7SupSDE0kxNGPsNWHB7t3G2EgUumidJ07zWYYeLCS5MVS81rEjNGUui6Rtk4HjZeDHI1CmfyjCWi6G8Tp9GXX9cbq0nAyE8qsZkwxuaiXfRyhSIuHetkeHsh/ZSxBZUQWZ+c="));
    }
}
