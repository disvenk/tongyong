package com.resto.shop.web.enums;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 入库单状态
 */
public class MaterialConsumeOptType extends Enumerable4IntValue {
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final long serialVersionUID = -2271531169560882201L;
    private static volatile transient Map<Integer, MaterialConsumeOptType> allbyvalue = new HashMap<Integer, MaterialConsumeOptType>();
    private static volatile transient Map<String, MaterialConsumeOptType> allbyname = new HashMap<String, MaterialConsumeOptType>();
    private static final Lock lock = new ReentrantLock();
//0-盘点统计 1-定时任务统计 2-主动统计
    public static MaterialConsumeOptType STOCK_COUNT = MaterialConsumeOptType.valueOf(0, "盘点统计");
    public static MaterialConsumeOptType SHEDULER = MaterialConsumeOptType.valueOf(1, "定时任务统计");
    public static MaterialConsumeOptType SUBJECT  = MaterialConsumeOptType.valueOf(2, "主动统计");//拒绝


    private MaterialConsumeOptType(int value, String name) {
        super(value, name);
    }

    public static MaterialConsumeOptType valueOf(int value, String name) {
        MaterialConsumeOptType e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<Integer, MaterialConsumeOptType> allbyvalue_new = new HashMap<Integer, MaterialConsumeOptType>();
        Map<String, MaterialConsumeOptType> allbyname_new = new HashMap<String, MaterialConsumeOptType>();
        e = new MaterialConsumeOptType(value, name);
        lock.lock();
        try {
            allbyvalue_new.putAll(allbyvalue);
            allbyname_new.putAll(allbyname);
            allbyvalue_new.put(value, e);
            allbyname_new.put(name, e);
            allbyvalue = allbyvalue_new;
            allbyname = allbyname_new;
        }
        finally {
            lock.unlock();
        }
        return e;
    }


    public static MaterialConsumeOptType valueOf(int value) {
        MaterialConsumeOptType e = allbyvalue.get(value);
        if (e != null) {
            return e;
        }
        else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(int value) {
        MaterialConsumeOptType e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static MaterialConsumeOptType[] values() {
        return allbyvalue.values().toArray(new MaterialConsumeOptType[0]);
    }

    public static void main(String args[]) {
        System.out.println(MaterialConsumeOptType.values()[0].getValue());
    }
}
