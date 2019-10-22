package com.resto.wechat.web.controller;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.DownLoadPdf;
import com.resto.brand.core.util.OhMyEmail;
import com.resto.brand.core.util.PdfCreateUtils;
import com.resto.brand.core.util.UrlUtils;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ElectronicTicketConfig;
import com.resto.brand.web.service.ElectronicTicketConfigService;
import com.resto.shop.web.dto.ReceiptOrder;
import com.resto.shop.web.model.Receipt;
import com.resto.shop.web.model.ReceiptTitle;
import com.resto.shop.web.service.ReceiptService;
import com.resto.shop.web.service.ReceiptTitleService;
import com.resto.wechat.web.config.SessionKey;
import com.resto.wechat.web.form.ReceiptForm;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.util.QrCodeUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by xielc on 2017/9/5.
 * 发票管理控制层
 */
@RestController
@RequestMapping("receipt")
public class ReceiptController extends GenericController{
    @Resource
    ReceiptService receiptService;
    @Resource
    ReceiptTitleService receiptTitleService;

    @Autowired
    private ElectronicTicketConfigService electronicTicketConfigService;

    /**
     * 根据会员id查询发票抬头列表
     * @return
     */
    @RequestMapping(value = "receiptTitleList")
    @ResponseBody
    public void receiptTitleList(HttpServletRequest request, HttpServletResponse response, String customer_id) throws IOException {
        AppUtils.unpack(request, response);
        List<ReceiptTitle> list = receiptTitleService.selectOneList(customer_id);
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
     * 根据会员id、抬头类型查询发票抬头列表
     * @return
     */
    @RequestMapping(value = "receiptTitleTypeList")
    @ResponseBody
    public void receiptTitleTypeList(HttpServletRequest request, HttpServletResponse response, String customer_id, String type) throws IOException {
        AppUtils.unpack(request, response);
        List<ReceiptTitle> list = receiptTitleService.selectTypeList(customer_id,type);
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
     * 根据会员id查询默认发票抬头
     * @return
     */
    @RequestMapping(value = "receiptTitleDefaultList")
    @ResponseBody
    public void receiptTitleDefaultList(HttpServletRequest request, HttpServletResponse response, String customer_id) throws IOException {
        AppUtils.unpack(request, response);
        List<ReceiptTitle> list = receiptTitleService.selectDefaultList(customer_id);
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
     * 用户插入发票抬头
     * @return json
     */
    @RequestMapping(value = "insertReceiptTitle")
    @ResponseBody
    public void insertReceiptTitle(HttpServletRequest request,HttpServletResponse response,ReceiptTitle receiptTitle) throws IOException {
        AppUtils.unpack(request, response);
        //receiptTitle.setCustomerId(getCurrentCustomerId());
        int count=receiptTitleService.insertSelective(receiptTitle);
        if(count==0){
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "失败,传入参数错误");
            json.put("success", false);
            json.put("data", "error");
            ApiUtils.setBackInfo(response, json);
        }else{
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "成功");
            json.put("success", true);
            json.put("data", "success");
            ApiUtils.setBackInfo(response, json);
        }
    }
    /**
     * 更新发票抬头
     * @return json
     */
    @RequestMapping(value = "updateReceiptTitle")
    @ResponseBody
    public void updateReceiptTitle(HttpServletRequest request,HttpServletResponse response,ReceiptTitle receiptTitle) throws IOException {
        AppUtils.unpack(request, response);
        receiptTitle.setUpdateTime(new java.sql.Date(System.currentTimeMillis()));
        int count= receiptTitleService.updateByPrimaryKeySelective(receiptTitle);
        if(count==0){
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "失败,传入参数错误");
            json.put("success", false);
            json.put("data", "error");
            ApiUtils.setBackInfo(response, json);
        }else{
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "成功");
            json.put("success", true);
            json.put("data","success");
            ApiUtils.setBackInfo(response, json);
        }
    }

    /**
     * 根据会员id、发票状态查询订单,历史
     * @return
     */
    @RequestMapping(value = "receiptOrderList")
    @ResponseBody
    public void receiptOrderList(HttpServletRequest request, HttpServletResponse response, String customer_id,String state,Integer ticketType,String shopId) throws IOException {
        AppUtils.unpack(request, response);
         ticketType = ticketType==1?0: 1;
        List<ReceiptOrder> list = receiptService.selectReceiptOrderList(customer_id,shopId,state,ticketType);
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
     * 用户插入发票信息
     * @return json
     */
    @RequestMapping(value = "insertReceipt")
    @ResponseBody
    public void insertReceipt(HttpServletRequest request,HttpServletResponse response,String orderNumber,String payTime,String orderMoney,String receiptMoney,String receiptTitleId,String state,String shopId) throws IOException, ParseException {
        AppUtils.unpack(request, response);
        Receipt receipt=new Receipt();
        receipt.setOrderNumber(orderNumber);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        receipt.setPayTime(sdf.parse(payTime));
        BigDecimal bd=new BigDecimal(orderMoney);
        receipt.setOrderMoney(bd);
        BigDecimal bp=new BigDecimal(receiptMoney);
        receipt.setReceiptMoney(bp);
        receipt.setReceiptTitleId(Long.parseLong(receiptTitleId.trim()));
        receipt.setState(Integer.parseInt(state.trim()));
        receipt.setShopId(shopId);
        receipt.setTicketType(0);
        Receipt count=receiptService.insertSelective(receipt);
        if(count==null){
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "失败,传入参数错误");
            json.put("success", false);
            json.put("data", "error");
            ApiUtils.setBackInfo(response, json);
        }else{
            JSONObject json = new JSONObject();
            json.put("code", 200);
            json.put("message", "成功");
            json.put("success", true);
            json.put("data", "success");
            ApiUtils.setBackInfo(response, json);
        }
    }
    @RequestMapping(value = "/qrcode")
    private ModelAndView createQrCode(String content, int width, int height, HttpServletResponse response) {
        BufferedImage bi = QrCodeUtils.encodePRToBufferedImage(content, width,height);
        try {
            response.setContentType("image/jpeg");
            OutputStream os = response.getOutputStream();
            ImageIO.write(bi, "jpg", os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(500);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    /**
    *@Description:电子发票申请开票
    *@Author:disvenk.dai
    *@Date:19:22 2018/6/8 0008
    */
    @RequestMapping("TicketToMany")
    @ResponseBody
    public void TicketToMany(HttpServletRequest request, HttpServletResponse response, @RequestBody ReceiptForm receiptForm){
        AppUtils.unpack(request, response);
        String brandId =  getRequest().getSession().getAttribute(SessionKey.CURRENT_BRAND_ID).toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            receiptForm.receipt.setPayTime(sdf.parse(receiptForm.receipt.getPayTimeStr()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        URI uri = UrlUtils.getIP(URI.create(request.getRequestURL().toString()));
        Boolean aBoolean = receiptService.ticketToMany(receiptForm.receipt, receiptForm.title,brandId, uri.toString());
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", "成功");
        json.put("success", aBoolean);
        json.put("data",aBoolean);
        ApiUtils.setBackInfo(response, json);
    }

    /**
    *@Description:下载pdf
    *@Author:disvenk.dai
    *@Date:15:50 2018/6/14 0014
    */
    @RequestMapping("downLoadPdf")
    public void downLoad(HttpServletRequest request,HttpServletResponse response,
                           String serial,String platformTid,String payeeRegisterNo){
        AppUtils.unpack(request, response);
        DownLoadPdf.DownLoad(response, request,serial,platformTid,payeeRegisterNo);
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", "成功");
        json.put("success", true);
        json.put("data","success");
        ApiUtils.setBackInfo(response, json);
    }

    /**
    *@Description:发送邮件
    *@Author:disvenk.dai
    *@Date:16:01 2018/6/14 0014
    */
    @RequestMapping("sendEmail")
    public Result sendEmail(String emailStr,Long ticketId) {
        Brand brand = (Brand) getRequest().getSession().getAttribute(SessionKey.CURRENT_BRAND);
        Receipt receipt = receiptService.selectById(ticketId);
        ElectronicTicketConfig electronicTicketConfig = electronicTicketConfigService.selectByBrandId(brand.getId());
        String path = PdfCreateUtils.getTicket(receipt.getSerialNo(), receipt.getOrderNumber(), electronicTicketConfig.getPayeeRegisterNo());
        boolean b = OhMyEmail.sendEmail(electronicTicketConfig.getEmail(),electronicTicketConfig.getAuthorizationKey(),"发票发送通知",brand.getBrandName(),emailStr,"您申请的电子发票已开出，请点击附件查看并核对发票明细","",new File(path));
        if (b) {
            File file = new File(path);
            file.delete();
            return Result.getSuccess();
        }
        return new Result(false);
    }
}
