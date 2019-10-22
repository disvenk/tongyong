package com.resto.brand.core.util;

import org.apache.log4j.Level;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/11/14/0014 14:38
 * @Description:
 */
public class SpecialLevel extends Level {
    public SpecialLevel(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }
}
