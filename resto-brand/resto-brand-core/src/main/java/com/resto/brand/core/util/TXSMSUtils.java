//package com.resto.brand.core.util;
//import com.alibaba.fastjson.JSONObject;
//import com.resto.brand.core.qroud.SmsMultiSender;
//import com.resto.brand.core.qroud.SmsMultiSenderResult;
//import com.resto.brand.core.qroud.SmsSingleSender;
//import com.resto.brand.core.qroud.SmsSingleSenderResult;
//
//import java.util.ArrayList;
//
//
//public class TXSMSUtils {
//    public final static int IDENTIFYING_CODE = 18776;  //验证码短信模板
//    public final static int DAYDATAMESSAGE = 18787;//日结数据短信
//    public final  static int DAYSCOREMESSAGE = 20389;//日结分数短信
//    public final static int XUNDATAMESSAGE = 20749;//旬结数据短信
//    public final  static int XUNSCOREMESSAGE = 20757;//旬结分数短信
//    public final static int MONTHDATAMESSAGE = 20753;//月结数据短信
//    public final  static int MONTHSCOREMESSAGE = 20759;//月结分数短信
//
//    private final static int APPID= 1400030364;
//	private final static String APPKEY = "3a424fc6a41e6339c5f9c7ac1ab9ce1e";
//	private final  static String SIGN = "餐加";//签名
//
//    public TXSMSUtils() throws Exception {
//    }
//
//    /**
//     *指定模板单发
//     * @return
//     */
//    public static SmsSingleSenderResult sendTmpSingle(String phone,Integer tmplId,ArrayList<String> params){
//        SmsSingleSenderResult singleSenderResult =null;
//        try{
//            //初始化单发
//            SmsSingleSender singleSender = new SmsSingleSender(APPID, APPKEY);
//            singleSenderResult = singleSender.sendWithParam("86", phone, tmplId, params, SIGN, "", "");
//        }catch (Exception e){
//
//        }
//        return singleSenderResult;
//    }
//
//    /**
//     *指定模板群发
//     * @return
//     */
//    public static SmsMultiSenderResult sendTmpQun(ArrayList<String> telehones, Integer tmplId, ArrayList<String> params){
//        SmsMultiSenderResult multiSenderResult=null;
//        // 初始化群发
//        try{
//            SmsMultiSender multiSender = new SmsMultiSender(APPID, APPKEY);
//            multiSenderResult = multiSender.sendWithParam("86", telehones, tmplId, params, SIGN, "", "");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return  multiSenderResult;
//    }
//
//
//
//    public static void main(String[] args) {
//        //测试单发
////        ArrayList<String> singleParma = new ArrayList<>();
////        singleParma.add("2345");
////        singleParma.add("简厨");
////        SmsSingleSenderResult smsSingleSenderResult = TXSMSUtils.sendTmpSingle("13317182430",18776,singleParma);
////        System.out.println(JSONObject.toJSONString(smsSingleSenderResult));
//
//        //测试群发
////        ArrayList<String> params = new ArrayList<>();
////        params.add("测试店铺");
////        params.add("2017-05-13");
////        params.add("5");
////        params.add("晴");
////        params.add("31℃");
////        params.add("20");
////        params.add("1000");
////        params.add("30");//
////        params.add("800");
////        params.add("90%");//10
////        params.add("50%");
////        params.add("50%");
////        params.add("20");
////        params.add("500");
////        params.add("10");
////        params.add("250");
////        params.add("10");
////        params.add("250");
////        params.add("30");
////        params.add("600");//20
////        params.add("20");
////        params.add("200");
////        params.add("30");
////        params.add("300");
////        params.add("500");
////        params.add("600");
////        params.add("700");
////        params.add("60%");
////        params.add("20000");
////        params.add("900000");//30
////        params.add("100000");
////        params.add("20000");
////
//        ArrayList<String> telephones = new ArrayList<>();
//        telephones.add("13317182430");
//        telephones.add("13627626221");
////        SmsMultiSenderResult smsMultiSenderResult =  TXSMSUtils.sendTmpQun(telephones,18787,params);
////        System.out.println(JSONObject.toJSONString(smsMultiSenderResult));
//
//        //测试日结分数短信(top10)
//
//        ArrayList<String> fenshuList = new ArrayList<>();
//        fenshuList.add("测试店铺");
//        fenshuList.add("2017-05-13");
//        fenshuList.add("5");
//        fenshuList.add("晴");
//        fenshuList.add("31℃");//5
//        fenshuList.add("20");
//        fenshuList.add("30");
//        fenshuList.add("40");
//
//        fenshuList.add("80");
//        fenshuList.add("90");
//        fenshuList.add("80");
//
//        StringBuilder s1 = new StringBuilder();
//        s1.append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120");
//
//        StringBuilder s2 = new StringBuilder();
//        s2.append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120").append("\n")
//                .append("测试单品1").append(":").append("120");
//
//        fenshuList.add(s1.toString());
//        fenshuList.add(s2.toString());
//
//        SmsMultiSenderResult smsMultiSenderResult2 = TXSMSUtils.sendTmpQun(telephones, 20389,fenshuList);
//        System.out.println(JSONObject.toJSONString(smsMultiSenderResult2));
//
//
//
//    }
//
//
//
//}
