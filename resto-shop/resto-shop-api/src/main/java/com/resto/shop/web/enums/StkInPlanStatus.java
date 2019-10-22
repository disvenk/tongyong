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
public class StkInPlanStatus extends Enumerable4StringValue {
    private static final Logger log = LoggerFactory.getLogger(Enumerable4StringValue.class);
    private static final long serialVersionUID = -2271531169560882201L;
    private static volatile transient Map<String, StkInPlanStatus> allbyvalue = new HashMap<String, StkInPlanStatus>();
    private static volatile transient Map<String, StkInPlanStatus> allbyname = new HashMap<String, StkInPlanStatus>();
    private static final Lock lock = new ReentrantLock();

    public static StkInPlanStatus INIT = StkInPlanStatus.valueOf("11", "待审核");
    public static StkInPlanStatus PASSED = StkInPlanStatus.valueOf("12", "审核通过");
    public static StkInPlanStatus REJECTED  = StkInPlanStatus.valueOf("13", "已驳回");//拒绝
    public static StkInPlanStatus FAILED = StkInPlanStatus.valueOf("14", "审核失败");
    public static StkInPlanStatus EXPIRED  = StkInPlanStatus.valueOf("15", "已失效");//超过有效期

    private StkInPlanStatus(String value, String name) {
        super(value, name);
    }

    public static StkInPlanStatus valueOf(String value, String name) {
        StkInPlanStatus e = allbyvalue.get(value);
        if (e != null) {
            if (e.name.equals(name) || undefined.equals(name))
                //undefined可以更新， 其他的name不可以更新？ No, 所有值都可以更新; 但是不能用undefined覆盖已有值
                return e;
            else {
                //命名不相同
                log.warn("Name to be change. value:" + value + ", old name:" + e.name + ", new name:" + name);
            }
        }

        Map<String, StkInPlanStatus> allbyvalue_new = new HashMap<String, StkInPlanStatus>();
        Map<String, StkInPlanStatus> allbyname_new = new HashMap<String, StkInPlanStatus>();
        e = new StkInPlanStatus(value, name);
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


    public static StkInPlanStatus valueOf(String value) {
        StkInPlanStatus e = allbyvalue.get(value);
        if (e != null) {
            return e;
        }
        else {
            return valueOf(value, undefined);
        }
    }

    public static boolean containValue(String value) {
        StkInPlanStatus e = allbyvalue.get(value);
        if (e != null) {
            return true;
        } else {
            return false;
        }
    }

    public static StkInPlanStatus[] values() {
        return allbyvalue.values().toArray(new StkInPlanStatus[0]);
    }

    public static void main(String args[]) {
        System.out.println(StkInPlanStatus.values()[0].getValue());
    }
}
