package com.resto.brand.core.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ThirdPatyUtils {

    //static  final  String Appkey="f63df5dcae5ce92e9c078d651dd8c8bd"; 测试生态
    static  final  String Appkey = "7B622C70C058F881A28CDC563977506B";//测试嫩绿
   // static  final  String Appkey = "617a855c49d2ee4749ed3f052a950326";//测试简厨
    /**
     *
     * 判断请求过来的url是否合法
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp,String nonce,List<String> appIdList) {
        boolean flag = false;
        for(String s:appIdList){
            if (signature == null || timestamp == null || nonce == null) {
                return false;
            }
            String[] arr = new String[]{s, timestamp, nonce};
            // 将 Appkey, timestamp, nonce 三个参数进行字典排序
            Arrays.sort(arr);
            StringBuilder content = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                content.append(arr[i]);
            }
            MessageDigest md = null;
            String tmpStr = null;
            try {
                md = MessageDigest.getInstance("SHA-1");
                // 将三个参数字符串拼接成一个字符串进行 shal 加密
                byte[] digest = md.digest(content.toString().getBytes());
                tmpStr = byteToStr(digest);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            content = null;
            // 将sha1加密后的字符串可与signature对比，标识该请求来自第三方的嫩绿茶
           // return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
           if(tmpStr.equals(signature.toUpperCase())) {
               flag=true;
               break;
           }
        }
        return  flag;
    }

    /**
     * 生成第三方签名用于测试
     * @param timestamp
     * @param nonce
     * @return
     */
    public  static  String getSign(String timestamp,String nonce){
         String [] arr = new String[]{Appkey,timestamp,nonce};
         //将三个参数进行字典排序
        Arrays.sort(arr);
        //将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<arr.length;i++){
            sb.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(sb.toString().getBytes());
            tmpStr = byteToStr(digest);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return  tmpStr;
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

    /**
     * 测试第三接口 的sign
     * @param args
     */
        public static void main(String[] args) {
            //生成signature
            String timestamp =  System.currentTimeMillis()+"";//1.当前时间搓
            String nonce =   RandomStringUtils.randomNumeric(4);//2.生成4位随机数
            timestamp="1490853502000";
            nonce="6983";
            String signature =ThirdPatyUtils.getSign(timestamp,nonce);
            System.out.println("timestamp:"+timestamp);//1490149284112
            System.out.println("nonce:"+nonce);//4262
            System.out.println("signature:"+signature);//ABAD5308683D24713637B433A3C30A4C7F9B46FB


            //测试验签
            List<String> appidList = new ArrayList<>();
            appidList.add("7B622C70C058F881A28CDC563977506B");
            System.err.println("验签结果为:"+checkSignature(signature,timestamp,nonce,appidList));

    }

//    //测试获取第三方接口
//    public static void main(String[] args) {
//
//
//
//
//
//    }



}
