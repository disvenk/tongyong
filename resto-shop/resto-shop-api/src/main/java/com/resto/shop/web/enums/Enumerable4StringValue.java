package com.resto.shop.web.enums;

import java.io.Serializable;

/**
 * 可动态增加取值的enum；
 * enum的缺点：在静态化的类级别预先框死了所有枚举项，无法更改，无法新增，无法识别新值。
 * 1. 在RPC的场景下，enmu的枚举值一旦定义后，同样的含义（int值）想改变字符值会造成反序列化失败。
 * 除非所有的client端和server端一起升级
 * 2. enmu只要增加一个枚举项就一定要修改代码，至少做一次发布
 * 3. enmu遇到数据库新的type取值（int）而枚举类未有定义，则valueOf一定会报错
 * <p/>
 * 这个可动态扩展的枚举解决上述所有问题：
 * 1. 不需要在静态化的类级别预先框死所有枚举项。只要给出一个数字和字符名称（value和name）就可立即新增一项枚举值
 * 2. 相对于直接用int作为type/status等场景的类型，给出一个readable name便于展示管理的命名和语义统一。
 * 3. 对于新定义的值，某个系统还未升级时，仍然能识别。 value为主，name弱约束
 * <p/>
 * 缺点：
 * 由于没有语言级别的支持，需要每个子类自己管理枚举，遍历等
 *
 */
public class Enumerable4StringValue implements Serializable {
    private static final long serialVersionUID = -9115042778813368152L;
    protected static final String undefined = "undefined";
    public final String value;
    public final String name;

    public Enumerable4StringValue(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return name;
    }

    @Override
    public String toString() {
        return value;
    }


    @Override
    public int hashCode() {
        return value.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Enumerable4StringValue)) {
            return false;
        }
        Enumerable4StringValue o = (Enumerable4StringValue) obj;
        return value == o.value;
    }
}
