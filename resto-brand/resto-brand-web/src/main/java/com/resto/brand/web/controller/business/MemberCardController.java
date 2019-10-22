package com.resto.brand.web.controller.business;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.service.WeChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by xielc on 2017/9/25.
 */
@Controller
@RequestMapping("memberCard")
public class MemberCardController extends GenericController {

    static final String GET_ACTIVATEUSER_URL = "https://api.weixin.qq.com/card/membercard/activateuserform/set?access_token=TOKEN";
    static final String GET_ACTIVATE_URL = "https://api.weixin.qq.com/card/membercard/activate/geturl?access_token=ACCESS_TOKEN";

    @Resource
    private WeChatService weChatService;

    @RequestMapping("/list")
    public void list() {
    }

    @RequestMapping("/token")
    @ResponseBody
    public String token(String appid,String secret){
        return weChatService.getAccessToken(appid.trim(),secret.trim());
    }
    @RequestMapping("/cardUrl")
    @ResponseBody
    public String cardUrl(String appid,String secret,String cardId){
        return weChatService.getOpenMemberCardUrl(appid, secret, cardId);
    }
}
