package com.resto.shop.web.enums;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 供应商报价单状态
 */
public class SupplierPriceStatus extends Enumerable4StringValue {
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final long serialVersionUID = -2271531169560882201L;
    private static volatile transient Map<String, SupplierPriceStatus> allbyvalue = new HashMap<String, SupplierPriceStatus>();
    private static volatile transient Map<String, SupplierPriceStatus> allbyname = new HashMap<String, SupplierPriceStatus>();
    private static final Lock lock = new ReentrantLock();

    public static SupplierPriceStatus INIT = SupplierPriceStatus.valueOf("11", "待审核");
    public static SupplierPriceStatus PASSED = SupplierPriceStatus.valueOf("12", "审核通过");
    public static SupplierPriceStatus REJECTED  = SupplierPriceStatus.valueOf("13", "已驳回");//拒绝
    public static SupplierPriceStatus FAILED = SupplierPriceStatus.valueOf("14", "审核失败");
    public static SupplierPriceStatus EXPIRED  = SupplierPriceStatus.valueOf("15", "已失效");//超过有效期

    private SupplierPriceStatus(String value, String name) {
        super(value, name);
    }

    public static SupplierPriceStatus valueOf(String value, String name) {
        SupplierPriceStatus e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, SupplierPriceStatus> allbyvalue_new = new HashMap<String, SupplierPriceStatus>();
        Map<String, SupplierPriceStatus> allbyname_new = new HashMap<String, SupplierPriceStatus>();
        e = new SupplierPriceStatus(value, name);
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


    public static SupplierPriceStatus valueOf(String value) {
        SupplierPriceStatus e = allbyvalue.get(value);
        if (e != null) {
            return e;
        }
        else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        SupplierPriceStatus e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static SupplierPriceStatus[] values() {
        return allbyvalue.values().toArray(new SupplierPriceStatus[0]);
    }

    public static void main(String args[]) {
        System.out.println(SupplierPriceStatus.values()[0].getValue());
    }
}
