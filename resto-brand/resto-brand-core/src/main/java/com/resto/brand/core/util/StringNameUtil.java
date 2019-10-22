package com.resto.brand.core.util;
/**
 * 字符串相关方法
 *
 */
public class StringNameUtil {

    //恋上桑格利亚·活氧[冷饮](少糖)(少冰)
    public static String getName(String str){
        //获取"("第一次出现的位置
        int index= str.indexOf("(");
        if(index<0){
            System.out.println(str);
            return str;
        }
        String temp=str.substring(0,index);
        return temp;

    }

    public  static String getAttr(String str){
        //获取"("第一次出现的位置
        int index= str.indexOf("(");
        if(index<0){
            System.out.println("---");
            return "";
        }
        //左圆括号的转义：( ==> u0028
       // 右圆括号的转义：) ==> u0029
        String temp=str.substring(index,str.length());
       temp= temp.replaceAll("\\(","").replaceAll("\\)",",");
       temp=temp.substring(0,temp.length()-1);
        return temp;
    }

    public static void main(String[] args) {
        String name = "恋上桑格利亚·活氧[冷饮](少糖)(少冰)";
        getName(name);
        getAttr(name);
    }

}
