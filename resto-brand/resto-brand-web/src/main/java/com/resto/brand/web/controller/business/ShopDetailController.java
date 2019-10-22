package com.resto.brand.web.controller.business;

import com.google.zxing.WriterException;
import com.resto.brand.core.entity.PictureResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.FileUpload;
import com.resto.brand.core.util.SHA1Util;
import com.resto.brand.core.util.StringUtils;
import com.resto.brand.core.util.UploadFilesUtil;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.*;
import com.resto.brand.web.model.WechatConfig;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("shopdetail")
public class ShopDetailController extends GenericController {

    @Resource
    ShopDetailService shopdetailService;

    @Resource
    WechatConfigService wechatConfigService;

    @Autowired
    RedisService redisService;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    BrandService brandService;

    @RequestMapping("/list")
    public void list() {
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<ShopDetail> listData() {
        List<ShopDetail> lists = shopdetailService.selectList();
        System.out.println(lists);
        return lists;
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(String id) {
        ShopDetail shopdetail = shopdetailService.selectById(id);
        return getSuccessResult(shopdetail);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid ShopDetail shopDetail) {
        String brandId = shopDetail.getBrandId();
        Brand brand = brandService.selectByPrimaryKey(brandId);
        redisService.clean(brand.getId(), brand.getBrandSign());

        if (shopDetail.getEnableDuoDongXian()==0){
            shopDetail.setSplitKitchen(1);
            shopDetail.setPrintType(1);
            shopdetailService.insertShopDetail(shopDetail);
        }else{
            shopDetail.setSplitKitchen(0);
            shopDetail.setPrintType(0);
            shopdetailService.insertShopDetail(shopDetail);
        }
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid ShopDetail brand) {
        brand.setLimit(1);
        if (brand.getEnableDuoDongXian()==0){
            brand.setSplitKitchen(1);
            brand.setPrintType(1);
            shopdetailService.update(brand);
        }else{
            brand.setSplitKitchen(0);
            brand.setPrintType(0);
         shopdetailService.update(brand);
        }
        return Result.getSuccess();
    }

    @RequestMapping("cleanShopDetail")
    @ResponseBody
    public Result cleanShopDetail(String id) {
//        RedisUtil.cleanShopDetail(id);
        redisService.cleanShopDetail(id);
        return Result.getSuccess();
    }

    /**
     * 假删除
     *
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String id) {
        shopdetailService.deleteBystate(id);
        return Result.getSuccess();
    }

    /**
     * 美食广场生存店铺二维码
     *
     * @return
     */
    @RequestMapping("sevenModeQRCode")
    @ResponseBody
    public String download(String shopId, HttpServletRequest request, HttpServletResponse response) throws IOException, WriterException {
        ShopDetail shopDetail = shopdetailService.selectByPrimaryKey(shopId);
//		 Brand brand = brandService.selectByPrimaryKey(shopDetail.getBrandId());
        WechatConfig wechatConfig = wechatConfigService.selectByBrandId(shopDetail.getBrandId());
        String token = weChatService.getAccessToken(wechatConfig.getAppid(), wechatConfig.getAppsecret());
        org.json.JSONObject qrParam = new org.json.JSONObject();
        qrParam.put("QrShopId", shopId);
        String result = weChatService.getParamQrCode(token, qrParam.toString());//二维码的附带参数字符串类型，长度不能超过64
        org.json.JSONObject obj = new org.json.JSONObject(result);
        String img = obj.has("ticket")?obj.getString("ticket"):"";
        String conteng = weChatService.showQrcode(img);
        return conteng;
    }

    @RequestMapping("getCode")
    @ResponseBody
    public Result getCode(String id) {
        ShopDetail shopDetail = shopdetailService.selectById(id);
        if (StringUtils.isEmpty(shopDetail.getPosKey())) {
            int machineId = 1;//最大支持1-9个集群机器部署
            int hashCodeV = UUID.randomUUID().toString().hashCode();
            if(hashCodeV < 0) {//有可能是负数
                hashCodeV = - hashCodeV;
            }
            // 0 代表前面补充0
            // 4 代表长度为4
            // d 代表参数为正数型

            shopDetail.setPosKey(machineId + String.format("%015d", hashCodeV));
            shopdetailService.update(shopDetail);
            return new Result(machineId + String.format("%015d", hashCodeV),true);

        } else {
            return new Result(shopDetail.getPosKey(),true);
        }

    }

    @RequestMapping("uploadFile")
    @ResponseBody
    public String uploadFile(MultipartFile file, HttpServletRequest request) {
        String type = request.getParameter("type");
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0, lastR) + "/";
        String filePath = "upload/files/" + DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        File finalFile = FileUpload.fileUp(file, systemPath + filePath, UUID.randomUUID().toString(), type);
        PictureResult picResult = UploadFilesUtil.uploadPic(finalFile);
        return picResult.getUrl();
    }

    @RequestMapping("/getSign")
    @ResponseBody
    public String getSign(@RequestParam Map<String, String> map) {
        String sign = SHA1Util.meiTuanEncryptionByMap(map);
        return sign;
    }
}
