package com.resto.shop.web.util.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by zhaojingyang on 2015/8/16.
 */
public class EnvUtil implements InitializingBean {

    static Logger logger = LoggerFactory.getLogger(EnvUtil.class);

    private static int env;

    public void setEnv(int env) {
        EnvUtil.env = env;
    }

    public static int getEnv() {
        return EnvUtil.env;
    }

    public static EnvGroup getEnvGroup() {
        return EnvGroup.valueOf(getEnv());
    }

    public static boolean isOnline() {
        if (EnvGroup.ONLINE.value() == env || EnvGroup.PRE.value() == env) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            logger.warn("当前所在分组:" + getEnvGroup().commnet());
        } catch (Exception e) {
            String errorMsg = String.format("分组值[%s]非法!!!!", env);
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }
}