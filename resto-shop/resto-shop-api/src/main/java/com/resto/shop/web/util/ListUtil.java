package com.resto.shop.web.util;

import java.util.List;

/**
 * Created by zhaojingyang on 2015/4/16.
 */
public final class ListUtil {

    public static boolean isNullOrEmpty(List list) {
        return null == list ? true : list.size() == 0 ? true : false;
    }

    public static boolean isNotEmpty(List list) {
        return !isNullOrEmpty(list);
    }
}
