package com.resto.wechat.web.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignChooseCard {
	public static Map<String, String> sign(String appid,String card_id,String api_ticket) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        //String card_id="pmJofxJcPVwsUddcHkdKntr1roSc";
        String card_type="CASH";
        String string1;
        String signature = "";
        
        ArrayList<String> list=new ArrayList<String>();
        list.add(appid);
        list.add(api_ticket);
        list.add(nonce_str);
        list.add(timestamp);
        //list.add(card_id);
        list.add(card_type);
        Collections.sort(list);
        
        //注意这里参数名必须全部小写，且必须有序
        string1 = list.get(0)+list.get(1)+list.get(2)+list.get(3)+list.get(4);//+list.get(5);
        System.out.println("cardSign:------------->"+string1);
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        System.out.println("signatureChooseCard="+signature);
        
        //ret.put("card_id", card_id);
        ret.put("card_type", card_type);
        ret.put("timestamp", timestamp);
        ret.put("noncestr", nonce_str);
        ret.put("signature", signature);

        return ret;
    }

	private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

	private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
