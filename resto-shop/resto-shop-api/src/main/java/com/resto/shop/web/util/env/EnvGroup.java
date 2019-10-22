package com.resto.shop.web.util.env;

/**
 * Created by zhaojingyang on 2015/8/16.
 */
public enum EnvGroup {

    PROJECT(1, "项目"),

    DAILY(10, "日常"),

    INTEG(15, "集成"),

    PERF(16, "性能"),

    PRE(20, "预发"),

    ONLINE(30, "线上");

    private int value;
    private String comment;

    private EnvGroup(int value, String commment) {
        this.value = value;
        this.comment = commment;
    }

    public int value() {
        return this.value;
    }

    public String commnet() {
        return this.comment;
    }

    public static EnvGroup valueOf(int value) {
        for (EnvGroup t : values())
            if (t.value == value)
                return t;
        throw new IllegalArgumentException("Invaild value of EnvType");
    }
}
