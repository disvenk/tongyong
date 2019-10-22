package com.resto.wechat.web.validate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * 字符串处理类
 **/
public class StringUtil {

    private static final Logger log = Logger.getLogger(StringUtil.class.getName());

    /**
     * 移除字符串中的html代码<br/>
     * 
     * @param htmlStr
     *            可能带有html代码的字符串
     * @return 已经移除html代码后的字符�?
     **/
    public static String removeHTMLTag(String htmlStr) {
        if (htmlStr == null || htmlStr.trim().equals("")) { return ""; }
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        try {
            String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>";
            String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>";
            String regEx_html = "<[^>]+>";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll("");
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll("");
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll("");
            textStr = htmlStr.replaceAll("&nbsp;", "");
         //   textStr = htmlStr.replaceAll("&amp;", "");
            textStr = textStr.replaceAll("<", "");
            textStr = textStr.replaceAll(">", "");
            textStr = textStr.replaceAll("®", "");
        //    textStr = textStr.replaceAll("&", "");
         //   textStr = textStr.replaceAll(" ", "");
        } catch (Exception e) {
            log.error("Html2Text: " + e.getMessage());
        }
        return textStr;
    }
    
    /**
     * 判断字符串是否为�?
     * 
     * @param str
     *            �?要判断的字符�?
     * @return 为空返回true，不为空返回false
     **/
    public static boolean isNullOrEmpty(String str) {
        return (null == str || str.trim().equals(""));
    }
    /**
	 * 用于处理sql的特殊字�?
	 * @param usernews：所传内�?
	 * @return：处理过后的内容
	 */
	public static String checkSQLString(String usernews)
	{
		if(!"".equals(usernews)&&usernews!=null)
		{
			usernews=usernews.replaceAll("�?", "");
			usernews=usernews.replaceAll(" ", "");
			usernews=usernews.replaceAll(" ", "");
			System.out.println("圈子内容�?"+usernews);
			usernews = usernews.replaceAll("[\\pP|~|$|^|<|>|\\||\\+|=|~!@#$%^&*()_+～！＠＃￥％…�?�＆＊（）�?��?�＋＝－「�?�{}『�?�／。，？�?��??<>]*", "");
			System.out.println("过滤过后的�?�是�?"+usernews);
		}
		return usernews;
	}
	 /**
     * 判断字符传长度是否正�?
     * @param str �?要判断的字符�?
     * @param length �?要判断字符串的长�?
     * @return 为空返回0，长度不对返�?-1，长度正确返�?1
     **/
    public static int equalStringLength(String str, int length) {
        return StringUtil.isNullOrEmpty(str) ? 0 : str.length()==length ? 1 : -1;
    }
    /**
    * 过滤特殊字符�?.<br>
    * @param str 字符�?.
    * @return 返回值描�?.
    * @throws Exception 异常描述
     */
    public static String StringFilter(String str) {
        // 只允许字母和数字       
        // String   regEx  =  "[^a-zA-Z0-9]";                     
        // 清除掉所有特殊字�?  
    	if(isNullOrEmpty(str)) return str;
    	String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#�?%…�??&*（）—�??+|{}【�?��?�；：�?��?��?��?�，、？]";  
    	Pattern   p   =   Pattern.compile(regEx);     
    	Matcher   m   =   p.matcher(str);     
    	return   m.replaceAll("").trim();     
  }     
    /**判断字符串是否为数字*/
    public static boolean isNumeric(String str){
    	if(str == null) return false;
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
    public static String getRandomString(int length) { 
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"); 
        StringBuffer sb = new StringBuffer(); 
        Random r = new Random(); 
        int range = buffer.length(); 
        for (int i = 0; i < length; i ++) { 
            sb.append(buffer.charAt(r.nextInt(range))); 
        } 
        return sb.toString(); 
    }
    /**
     * 填充字符�?
     * */
    public static String fillStringWithLength(String str,char fillChar,boolean fillBefore,int length) {
        if(str.length() >= length){
        	return str;
        }
        int fillLen = length - str.length();
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ; i < fillLen ; i++){
        	sb.append(fillChar);
        }
        if(fillBefore){
        	sb.append(str);
        	return sb.toString();
        }else{
        	return str + sb.toString();
        }
    }
    public static boolean validPhoneNo(String phoneNo) {
		boolean flag = false;
		Pattern pattern = Pattern.compile("^[1][3,5,7,8]\\d{9}$");
		Matcher m = pattern.matcher(phoneNo);
		if (m.matches()) {
			return true;
		}
		return flag;
	}
    public String confuse(String str){
		List<String> list=Arrays.asList(str.split(""));
        Collections.shuffle(list);
        String out=new String();
        for(String s:list){
            out+=s;
        }
        return out;
	}
}
