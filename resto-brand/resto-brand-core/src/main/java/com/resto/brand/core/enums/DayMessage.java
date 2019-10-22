package com.resto.brand.core.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yz on 2017-03-13.
 *
 * 封装集合初始化大小，避免集合来回分配空间
 * 影响性能
 */
public class DayMessage {

    //本日之前出现的顾客(以店铺为基础) 参考简厨初始值设置为2000 以后修改
   public static final int customerBeforeToday =1000;

   //本日出现的顾客(去重)
    public  static  final  int customerInToday = 30;


    /**
     * 测试初始化大小对集合的影响
     * @param args
     */
    public static void main(String[] args) {

        Long begin = System.currentTimeMillis();

        List<String> arr = new ArrayList<>();
        for(int i=0;i<200000;i++){
            arr.add(i+"s");
        }
        Long end = System.currentTimeMillis();
        Long time = end-begin;
        System.out.println("不指定初始化大小耗时"+time);

        Long begin2 = System.currentTimeMillis();
        List<String> arr2 = new ArrayList<>(5000);
        for(int i=0;i<200000;i++){
            arr2.add(i+"s");
        }
        Long end2 = System.currentTimeMillis();
        Long time2 = end2-begin2;
        System.out.println("指定初始化大小后耗时"+time2);
    }


}





