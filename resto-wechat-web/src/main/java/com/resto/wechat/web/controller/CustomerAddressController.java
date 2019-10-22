package com.resto.wechat.web.controller;


import com.resto.api.customer.entity.CustomerAddress;
import com.resto.api.customer.service.NewCustomerAddressService;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.validate.CustomerAddressValidate;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * Created by xielc on 2017/6/12.
 */
@RestController
@RequestMapping("address")
public class CustomerAddressController extends GenericController{

    @Resource
    NewCustomerAddressService newCustomerAddressService;

    /**
     * 查询某个用户所有配送地址,以及默认送货地址排列在第一列
     * @return
     */
    @RequestMapping(value = "list")
    public @ResponseBody void listCustomerAddress(HttpServletRequest request, HttpServletResponse response, String customer_id) throws IOException {
        AppUtils.unpack(request, response);
        CustomerAddress customerAddress=new CustomerAddress();
        customerAddress.setCustomerId(customer_id);
        List<CustomerAddress> list = newCustomerAddressService.dbSelect(getCurrentBrandId(),customerAddress);
        JSONObject json = new JSONObject();
        if(list!=null&&!list.isEmpty()){
            json.put("code", 200);
            json.put("message", "成功");
            json.put("success", true);
            json.put("data", list);
            ApiUtils.setBackInfo(response, json);
        }else {
            json.put("code", 200);
            json.put("message", "查询失败，没有查询到数据");
            json.put("success", false);
            json.put("data", list);
            ApiUtils.setBackInfo(response, json);
        }
    }

    /**
     * 根据配送地址id查询具体配送信息
     *
     */
    @RequestMapping(value = "show")
    public @ResponseBody void show(HttpServletRequest request,HttpServletResponse response,String id) throws IOException {
        AppUtils.unpack(request, response);
        CustomerAddress customerAddress = newCustomerAddressService.dbSelectByPrimaryKey(getCurrentBrandId(),id);
        JSONObject json = new JSONObject();
        if(customerAddress!=null){
            json.put("code", 200);
            json.put("message", "成功");
            json.put("success", true);
            json.put("data", JSONObject.fromObject(customerAddress));
            ApiUtils.setBackInfo(response, json);
        }else{
            json.put("code", 200);
            json.put("message", "查询失败，没有查询到数据");
            json.put("success", false);
            json.put("data", JSONObject.fromObject(customerAddress));
            ApiUtils.setBackInfo(response, json);
        }
    }

    /**
     * 插入用户配送地址
     * @param customerAddress
     * @return
     */
    @RequestMapping(value = "insert")
    public @ResponseBody void insertCustomerAddress(HttpServletRequest request,HttpServletResponse response,CustomerAddress customerAddress) throws IOException {
        AppUtils.unpack(request, response);
        String errorText = "";
        errorText = errorText+ CustomerAddressValidate.customerAddressValidate(customerAddress);
        if(errorText.equals("")){
            customerAddress.setCustomerId(getCurrentCustomerId());
            //customerAddress.setCustomerId("4eb6153983ef49cfb04c2d26dee02174");
            int count=newCustomerAddressService.dbSave(getCurrentBrandId(),customerAddress);
            if(count==0){
                errorText="error";
                JSONObject json = new JSONObject();
                json.put("code", 200);
                json.put("message", "失败,传入参数错误");
                json.put("success", false);
                json.put("data", errorText);
                ApiUtils.setBackInfo(response, json);
            }else{
                errorText="success";
                JSONObject json = new JSONObject();
                json.put("code", 200);
                json.put("message", "成功");
                json.put("success", true);
                json.put("data", errorText);
                ApiUtils.setBackInfo(response, json);
            }
        }else {
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "失败,传入参数错误");
            json.put("success", false);
            json.put("data", errorText);
            ApiUtils.setBackInfo(response, json);
        }
    }

    /**
     * 更新配送地址
     * @param response
     * @param customerAddress
     */
    @RequestMapping(value = "update")
    public @ResponseBody void updateCustomerAddress(HttpServletRequest request,HttpServletResponse response,CustomerAddress customerAddress) throws IOException {
        AppUtils.unpack(request, response);
        String errorText = "";
        errorText = errorText+ CustomerAddressValidate.customerAddressValidate(customerAddress);
        if(errorText.equals("")){
            customerAddress.setCustomerId(getCurrentCustomerId());
            //customerAddress.setCustomerId("bca307f8b4464daf8f7c4ba00033747d");
            int count= newCustomerAddressService.dbUpdate(getCurrentBrandId(),customerAddress);
            if(count==0){
                errorText="error";
                JSONObject json = new JSONObject();
                json.put("code", 200);
                json.put("message", "失败,传入参数错误");
                json.put("success", false);
                json.put("data", errorText);
                ApiUtils.setBackInfo(response, json);
            }else{
                errorText="success";
                JSONObject json = new JSONObject();
                json.put("code", 200);
                json.put("message", "成功");
                json.put("success", true);
                json.put("data", errorText);
                ApiUtils.setBackInfo(response, json);
            }
        }else{
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "失败,传入参数错误");
            json.put("success", false);
            json.put("data", errorText);
            ApiUtils.setBackInfo(response, json);
        }

    }

}
