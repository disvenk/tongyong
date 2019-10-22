package com.resto.wechat.web.util;

import java.util.Random;


/**
 * Created by Administrator on 2015/1/13.
 * com.quancai.pm.utils
 * Description
 */
public class RandomUtil {

    Random rd = new Random();

    public int getRandom() {

        int iRandom = 0;
        do {
            iRandom = rd.nextInt(1000000);//6位
        } while (iRandom < 100000);
        return iRandom;
    }

}
