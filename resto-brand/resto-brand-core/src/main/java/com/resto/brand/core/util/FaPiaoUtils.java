package com.resto.brand.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.entity.HttpRequest;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaEinvoiceCreatereqRequest;
import com.taobao.api.request.AlibabaEinvoiceMerchantCreatereqRequest;
import com.taobao.api.request.AlibabaEinvoiceMerchantResultGetRequest;
import com.taobao.api.response.AlibabaEinvoiceMerchantCreatereqResponse;
import com.taobao.api.response.AlibabaEinvoiceMerchantResultGetResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import sun.net.www.http.HttpClient;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by disvenk.dai on 2018-06-12 14:39
 */
public class FaPiaoUtils {

    public static Boolean applyTicket(
                            String appKey,
                            String appSecret,
                            String platformTid,
                            String serialNo,
                            String payeeAddress,
                            String payeeName,
                            String payerRegisterNo,
                            String payeeOperator,
                            String invoiceAmount,
                            String price,
                            String tax,
                            String payeeRegisterNo,
                            String payerAddress,
                            String payerBankaccount,
                            String payerName,
                            String payerPhone,
                            String payeeChecker,
                            String payeeReceiver,
                            String payeePhone
                            ) {
        TaobaoClient client = new DefaultTaobaoClient("https://eco.taobao.com/router/rest",appKey,appSecret);
        AlibabaEinvoiceMerchantCreatereqRequest req = new AlibabaEinvoiceMerchantCreatereqRequest();
        req.setBusinessType(0L);
        req.setPlatformCode("OTHER");//电商平台代码
        req.setPlatformTid(platformTid);//订单号
        req.setSerialNo(serialNo);//开票流水号
        req.setPayeeAddress(payeeAddress);//开票方地址
        req.setPayeeName(payeeName);//开票方名称
        req.setPayerRegisterNo(payerRegisterNo);//付款方税务登记证号
        req.setPayeeOperator(payeeOperator);//开票人
        req.setInvoiceAmount(invoiceAmount);//价税合计

        List<AlibabaEinvoiceMerchantCreatereqRequest.InvoiceItem> list = new ArrayList<AlibabaEinvoiceMerchantCreatereqRequest.InvoiceItem>();
        AlibabaEinvoiceMerchantCreatereqRequest.InvoiceItem obj3 = new AlibabaEinvoiceMerchantCreatereqRequest.InvoiceItem();
        list.add(obj3);
        obj3.setItemName("餐饮费");//发票项目名称
        obj3.setPrice(price);//单价
        obj3.setQuantity("1");//数量
        obj3.setRowType("0");//发票性质填0
        obj3.setSumPrice(price);//总价
        obj3.setTax(tax);//税额
        obj3.setTaxRate("0.06");//税率
        obj3.setUnit("份");//单位
        obj3.setAmount(invoiceAmount);//税价合计

        req.setInvoiceItems(list);
        req.setInvoiceType("blue");//发票类型，蓝票
        req.setPayeeRegisterNo(payeeRegisterNo);//收款方税务登记证号
        req.setPayerAddress(payerAddress);//消费者地址
        req.setPayerBankaccount(payerBankaccount);//付款方开票开户银行账号
        req.setPayerName(payerName);//付款方名称, 对应发票台头
        req.setPayerPhone(payerPhone);//消费者联系电话
        req.setSumPrice(price);//合计金额
        req.setSumTax(tax);//合计税额
        req.setPayeeChecker(payeeChecker);//复核人
        req.setPayeeReceiver(payeeReceiver);//收款人
        req.setPayeePhone(payeePhone);//收款方电话
        AlibabaEinvoiceMerchantCreatereqResponse rsp = null;
        try {
            rsp = client.execute(req);
            if(rsp==null || rsp.getBody()==null){
                return false;
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());

        JSONObject jsonObject = JSONObject.parseObject(rsp.getBody());
        JSONObject jsonObject1 = (JSONObject) jsonObject.get("alibaba_einvoice_merchant_createreq_response");
        if(jsonObject1==null){
            return false;
        }
        Boolean is_success = (Boolean) jsonObject1.get("is_success");

        return is_success;
    }

    public static String getTicket(String serialNo,String platformTid,String payeeRegisterNo) {
        TaobaoClient client = new DefaultTaobaoClient("https://eco.taobao.com/router/rest", "24927248","7dce982afa9d0d7fc079fbe1c3417b6d");
        AlibabaEinvoiceMerchantResultGetRequest req = new AlibabaEinvoiceMerchantResultGetRequest();
        req.setSerialNo(serialNo);
        req.setPlatformCode("OTHER");
        req.setPlatformTid(platformTid);
        req.setPayeeRegisterNo(payeeRegisterNo);
        AlibabaEinvoiceMerchantResultGetResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
        JSONObject jsonObject = JSONObject.parseObject(rsp.getBody());
        JSONObject jsonObject1 = jsonObject.getJSONObject("alibaba_einvoice_merchant_result_get_response");
        if(jsonObject1==null){
            return null;
        }
        JSONObject jsonObject2 = jsonObject1.getJSONObject("invoice_result_list");
       JSONArray jsonObject3 = (JSONArray) jsonObject2.get("invoice_result");
        JSONObject jsonObject4 = (JSONObject) jsonObject3.get(0);
        String file_path = jsonObject4.getString("file_path");
        return file_path;
    }

    public static void main(String[] args) {
        String ticket = getTicket("201807051048250286","201807051039310404","91310230MA1K05DW59");
        System.out.println(ticket);
    }
}
