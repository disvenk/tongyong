package com.resto.pos.web.controller;

import com.resto.brand.core.util.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

/**
 * Created by KONATA on 2017/3/13.
 */
@RequestMapping("/log")
@Controller
public class LogController extends GenericController {

    @Value("#{configProperties['pos.action.url']}")
    private String url;



    @RequestMapping("/test")
    public void test() throws UnsupportedEncodingException {

        Map map = new HashMap(4);
        map.put("brandName", "似懂非懂");
        map.put("shopName", "test");
        map.put("content", "老丁丁");
        map.put("type", "test");
        doPostAnsc(map);

    }

}
