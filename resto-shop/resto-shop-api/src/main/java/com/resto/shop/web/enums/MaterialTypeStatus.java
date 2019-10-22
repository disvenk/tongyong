package com.resto.shop.web.enums;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 库存日结盘点状态
 */
public class MaterialTypeStatus extends Enumerable4StringValue {
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final long serialVersionUID = -2271531169560882201L;
    private static volatile transient Map<String, MaterialTypeStatus> allbyvalue = new HashMap<String, MaterialTypeStatus>();
    private static volatile transient Map<String, MaterialTypeStatus> allbyname = new HashMap<String, MaterialTypeStatus>();
    private static final Lock lock = new ReentrantLock();

    public static MaterialTypeStatus INGREDIENTS = MaterialTypeStatus.valueOf("INGREDIENTS", "主料");
    public static MaterialTypeStatus ACCESSORIES = MaterialTypeStatus.valueOf("ACCESSORIES", "辅料");
    public static MaterialTypeStatus SEASONING  = MaterialTypeStatus.valueOf("SEASONING", "调料");
    public static MaterialTypeStatus MATERIEL  = MaterialTypeStatus.valueOf("MATERIEL", "物料");


    private MaterialTypeStatus(String value, String name) {
        super(value, name);
    }

    public static MaterialTypeStatus valueOf(String value, String name) {
        MaterialTypeStatus e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, MaterialTypeStatus> allbyvalue_new = new HashMap<String, MaterialTypeStatus>();
        Map<String, MaterialTypeStatus> allbyname_new = new HashMap<String, MaterialTypeStatus>();
        e = new MaterialTypeStatus(value, name);
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


    public static MaterialTypeStatus valueOf(String value) {
        MaterialTypeStatus e = allbyvalue.get(value);
        if (e != null) {
            return e;
        }
        else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        MaterialTypeStatus e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static MaterialTypeStatus[] values() {
        return allbyvalue.values().toArray(new MaterialTypeStatus[0]);
    }

    public static void main(String args[]) {
        System.out.println(MaterialTypeStatus.valueOf("INGREDIENTS").getDescription());
    }
}
