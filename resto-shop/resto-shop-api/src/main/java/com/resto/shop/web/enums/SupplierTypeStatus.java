package com.resto.shop.web.enums;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 供应商类型
 */
public class SupplierTypeStatus extends Enumerable4StringValue {
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final long serialVersionUID = -2271531169560882201L;
    private static volatile transient Map<String, SupplierTypeStatus> allbyvalue = new HashMap<String, SupplierTypeStatus>();
    private static volatile transient Map<String, SupplierTypeStatus> allbyname = new HashMap<String, SupplierTypeStatus>();
    private static final Lock lock = new ReentrantLock();

    public static SupplierTypeStatus MATERIAL_CLASS = SupplierTypeStatus.valueOf("1", "物料类");
    public static SupplierTypeStatus SERVER_CLASS = SupplierTypeStatus.valueOf("2", "服务类");
    public static SupplierTypeStatus PROJECT_CLASS  = SupplierTypeStatus.valueOf("3", "工程类");


    private SupplierTypeStatus(String value, String name) {
        super(value, name);
    }

    public static SupplierTypeStatus valueOf(String value, String name) {
        SupplierTypeStatus e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, SupplierTypeStatus> allbyvalue_new = new HashMap<String, SupplierTypeStatus>();
        Map<String, SupplierTypeStatus> allbyname_new = new HashMap<String, SupplierTypeStatus>();
        e = new SupplierTypeStatus(value, name);
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


    public static SupplierTypeStatus valueOf(String value) {
        SupplierTypeStatus e = allbyvalue.get(value);
        if (e != null) {
            return e;
        }
        else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        SupplierTypeStatus e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static SupplierTypeStatus[] values() {
        return allbyvalue.values().toArray(new SupplierTypeStatus[0]);
    }



    public static void main(String args[]) {
        SupplierTypeStatus[] values = SupplierTypeStatus.values();
        for (SupplierTypeStatus v:values) {
            System.out.println(v.getValue());
            System.out.println(v.getDescription());

        }
        //System.out.println(SupplierTypeStatus.values()[0].getValue());
    }
}
