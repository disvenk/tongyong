package com.resto.shop.web.controller.business;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.model.MemberActivity;
import com.resto.shop.web.model.MemberActivityThing;
import com.resto.shop.web.service.CustomerService;
import com.resto.shop.web.service.MemberActivityService;
import com.resto.shop.web.service.MemberActivityThingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("memberActivity")
public class MemberActivityController extends GenericController {

    Logger log = LoggerFactory.getLogger(MemberActivityController.class);

    @Resource
    MemberActivityService memberActivityService;

    @Resource
    CustomerService customerService;

    @Resource
    MemberActivityThingService memberActivityThingService;

    @RequestMapping("/list")
    public void list(){
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<MemberActivity> listData(){
        return memberActivityService.selectList();
    }

    @RequestMapping("/create")
    @ResponseBody
    public Result create(@Valid MemberActivity memberActivity){
        memberActivity.setBrandId(getCurrentBrandId());
        memberActivityService.insert(memberActivity);
        return Result.getSuccess();
    }


    @RequestMapping("/modify")
    @ResponseBody
    public Result modify(@Valid MemberActivity memberActivity){
        memberActivityService.update(memberActivity);
        MemberActivityThing memberActivityThing = new MemberActivityThing();
        memberActivityThing.setDiscount(memberActivity.getDisconut());
        memberActivityThing.setActivityId(memberActivity.getId());
        memberActivityThingService.update(memberActivityThing);
        return Result.getSuccess();
    }


    /**
     * 查询到当前折扣下面的用户信息
     * @param id
     * @return
     */
    @RequestMapping("/selectCustomer")
    @ResponseBody
    public Result selectCustomer(Integer id){
        List<JSONObject> array = new ArrayList<>();
        try{
            array = memberActivityThingService.selectCustomerInfo(id);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询该折扣下面的会员信息出错" + e.getMessage());
        }
        return getSuccessResult(array);
    }

    /**
     *将telephone对应用户录入会员折扣
     * @param telephone
     * @param activityId
     * @return
     */
    @RequestMapping("/inputCustomer")
    @ResponseBody
    public Result inputCustomer(String telephone, Integer activityId){
        try{
            Customer customer = customerService.selectByTelePhone(telephone);
            if (customer != null){
                MemberActivity activity = memberActivityService.selectById(activityId);
                MemberActivityThing memberActivityThing = new MemberActivityThing();
                memberActivityThing.setActivityId(activityId);
                memberActivityThing.setCreateTime(new Date());
                memberActivityThing.setTelephone(telephone);
                memberActivityThing.setDiscount(activity.getDisconut());
                memberActivityThingService.insert(memberActivityThing);
            }else{
                return new Result(false, "该号码不存在对应会员");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("录入会员折扣出错" + e.getMessage());
        }
        return getSuccessResult();
    }

    /**
     * 会员折扣下的会员
     * @param id
     * @return
     */
    @RequestMapping("/deleteMemberActivityThing")
    @ResponseBody
    public Result deleteMemberActivityThing(Integer id){
        try{
            memberActivityThingService.delete(id);
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除会员折扣失败");
            return new Result(false);
        }
        return getSuccessResult();
    }
}
