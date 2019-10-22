package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.ChargeOrder;
import com.resto.shop.web.service.ChargeOrderService;

@Controller
@RequestMapping("chargeorder")
public class ChargeOrderController extends GenericController {

    @Resource
    ChargeOrderService chargeorderService;

    @RequestMapping("/list")
    public void list() {
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<ChargeOrder> listData() {

        return chargeorderService.selectList();
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(String id) {
        ChargeOrder chargeorder = chargeorderService.selectById(id);
        return getSuccessResult(chargeorder);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid ChargeOrder brand) {
        chargeorderService.insert(brand);
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid ChargeOrder brand) {
        chargeorderService.update(brand);
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String id) {
        chargeorderService.delete(id);
        return Result.getSuccess();
    }
}
