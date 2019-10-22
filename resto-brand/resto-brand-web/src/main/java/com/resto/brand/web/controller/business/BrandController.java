package com.resto.brand.web.controller.business;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.JsonUtils;
import com.resto.brand.core.util.QRCodeUtil;
import com.resto.brand.core.util.StringUtils;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.Contract;
import com.resto.brand.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping("brand")
public class BrandController extends GenericController {

    @Resource
    BrandService brandService;
    @Resource
    ModuleListService moduleListService;

    @Resource
    ContractService contractService;

    @Autowired
    RedisService redisService;

    @Autowired
    WeChatService weChatService;

    @RequestMapping("/list")
    public void list() {

    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<Brand> listData() {
        List<Brand> brands = brandService.selectBrandDetailInfo();
        for(int i = 0; i < brands.size(); i++){
            Brand brand = brands.get(i);
            brand.getDatabaseConfig().setUsername("就不告诉你");
            brand.getDatabaseConfig().setPassword("就不告诉你");
        }
        return brands;
    }

    @RequestMapping("/list_without_self")
    @ResponseBody
    public Result list_without_self() {
        List<Brand> lists = brandService.selectBrandDetailInfo();
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getId().equals(getCurrentBrandId())) {
                lists.remove(i);
            }
        }
        return getSuccessResult(lists);
    }


    /**
     * 此方法用于 在添加商家 用户时，查询所需要的参数包括（品牌信息，店铺信息）
     *
     * @return
     */
    @RequestMapping("/queryBrandAndShop")
    @ResponseBody
    public List<Brand> queryBrandAndShop() {
        return brandService.queryBrandAndShop();
    }

    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one(String id) {
        Brand brand = brandService.selectById(id);
        return getSuccessResult(brand);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@Valid Brand brand) {
        //设置设置当前操作的用户
        brand.setAddUser("超级管理员");
        brand.setUpdateUser("超级管理员");
        brandService.insertInfo(brand);
        return Result.getSuccess();
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid Brand brand) {
        //修改人 ID
        brand.setUpdateUser("超级管理员");
        brandService.updateInfo(brand);
        /*if(RedisUtil.get(brand.getId()+"info") != null){
            RedisUtil.remove(brand.getId()+"info");
        }*/
        return Result.getSuccess();
    }

    @RequestMapping("/savePhoneList")
    @ResponseBody
    public Result savePhoneList(String brandId,String phoneList) {
        //修改人 ID
        Brand brand = brandService.selectById(brandId);
        brand.setPhoneList(phoneList);
        brandService.update(brand);
        return Result.getSuccess();
    }

    @RequestMapping("/saveWhitePhoneList")
    @ResponseBody
    public Result saveWhitePhoneList(String brandId,String phoneList) {
        //修改人 ID
        Brand brand = brandService.selectById(brandId);
        brand.setWhitePhoneList(phoneList);
        brandService.update(brand);
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String id) {
        brandService.deleteInfo(id);
        return Result.getSuccess();
    }

    @RequestMapping("validateInfo")
    @ResponseBody
    public Result validateInfo(String brandSign, String brandId) {
        boolean flag = brandService.validataBrandInfo(brandSign, brandId);
        return new Result(flag);
    }

    @RequestMapping("modify_modulesetting")
    @ResponseBody
    public Result modify_modulesetting(String brandId, Integer[] moduleIds) {
        moduleListService.updateBrandModuleList(brandId, moduleIds);
        return Result.getSuccess();
    }

    @RequestMapping("list_hasmodule")
    @ResponseBody
    public Result list_hasmodule(String id) {
        List<Integer> hasModule = moduleListService.selectBrandHasModule(id);
        return getSuccessResult(hasModule);
    }


    /**
     * 得到所有合同信息
     * @return
     */
    @RequestMapping("/getContractInfo")
    @ResponseBody
    public Result getContractInfo(){
        try{
            List<Contract> contracts = contractService.selectList();
            return getSuccessResult(contracts);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false);
        }
    }


    @RequestMapping("getWxMpQrCode")
    @ResponseBody
    public void getWxMpQrCode(HttpServletResponse response,HttpServletRequest request,String appId,String secret) throws IOException {
        //验证
        if(StringUtils.isEmpty(appId) || StringUtils.isEmpty(secret)){
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<h1>参数错误~</h1>");
            return;
        }
        //获取Token
        String token = weChatService.getAccessToken(appId,secret);
        String qrParam = "-";
        if(token.indexOf("errcode")>0 || token.indexOf("errmsg")>0){
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(token);
            return;
        }
        //获取 qrCode
        JSONObject obj = JSONObject.parseObject(weChatService.getParamQrCode(token,qrParam));
        if(!obj.containsKey("url")){
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(obj.toJSONString());
            return;
        }
        //生成图片
        FileInputStream fis = null;
        File file = null;
        response.setContentType("image/gif");
        String fileName = System.currentTimeMillis()+"";
        try {
            QRCodeUtil.createQRCode(obj.getString("url"), getFilePath(request, null), fileName);
            OutputStream out = response.getOutputStream();
            file = new File(getFilePath(request, fileName));
            fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.gc();//手动回收垃圾，清空文件占用情况，解决无法删除文件
            file.delete();
        }
    }

//    public static void main(String[] args) {
//        String appid = "wx04a191553b57a2a6";
//        String appsecret = "3a6bc3af9165ee3241f33b17a11a4248";
//        String token =  WeChatUtils.getAccessToken(appid,appsecret);
//
//        String firstName = "粉丝圈";
//        String secondName = "点餐";
//        String thirdName= "我";
//        String brandsign = "mingdong";
//
//        String menu = "{" +
//                "     \"button\":[\n" +
//                "     {  \n" +
//                "          \"type\":\"view\",\n" +
//                "          \"name\":\""+firstName+"\",\n" +
//                "          \"url\":\"http://"+brandsign+".restoplus.cn/wechat/index?subpage=home\"\n" +
//                "      },\n" +
//                "         { \n" +
//                "          \"type\":\"view\",\n" +
//                "          \"name\":\""+secondName+"\",\n" +
//                "          \"url\":\"http://"+brandsign+".restoplus.cn/wechat/index?subpage=tangshi\"\n" +
//                "      },\n" +
//                "         { \n" +
//                "          \"type\":\"view\",\n" +
//                "          \"name\":\""+thirdName+"\",\n" +
//                "          \"url\":\"http://"+brandsign+".restoplus.cn/wechat/index?subpage=my\"\n" +
//                "      }]\n" +
//                "}";
//
//        System.out.println(menu);
//        String createUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+token;
//        TxSmsResult r = new TxSmsResult(true);
//
//        try{
//            URL url = new URL(createUrl);
//            HttpURLConnection http =   (HttpURLConnection) url.openConnection();
//            http.setRequestMethod("POST");
//            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//            http.setDoOutput(true);
//            http.setDoInput(true);
//            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//连接超时30秒
//            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒
//            http.connect();
//            OutputStream os= http.getOutputStream();
//            os.write(menu.getBytes("UTF-8"));//传入参数
//            os.flush();
//            os.close();
//            InputStream is =http.getInputStream();
//            int size =is.available();
//            byte[] jsonBytes =new byte[size];
//            is.read(jsonBytes);
//            String message=new String(jsonBytes,"UTF-8");
//            r.setMessage(message);
//            System.out.println(message);
//        }catch (Exception e){
//            r.setSuccess(false);
//            r.setMessage("创建菜单失败");
//            System.out.println("失败------------");
//        }
//
//    }

//    public static void main(String[] args) {
////        String accessJson = WeChatUtils.getUrlAccessToken("011lUkp51PZTBO18bVm51i7Cp51lUkp1", "wx36bd5b9b7d264a8c", "807530431fe6e19e3f2c4a7d1a149465");
////        System.out.println(accessJson);
//
//        BigDecimal a = new BigDecimal(10);
//        BigDecimal b = new BigDecimal(20);
//
//        System.out.println(a.divide(b,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
//
//    }

//    /**
//     * 清缓存
//     * @param args
//     */
//    public static void main(String args[]) {
//        //定义品牌
//        String brandId = "455b5a2215f7421e881dcb7034cdaa6b"+"setting";//测试专用品牌
//
//        //定义店铺
//        String shopId = "f48a0a35e0be4dd8aaeb7cf727603958"+"info"; //测试店铺
//
//        //定义店铺集合
//        String shopList ="455b5a2215f7421e881dcb7034cdaa6b"+"shopList";
//
//        //清品牌
//        //cleanBrandSetting(brandId);
//
//        //清店铺
//        cleanShopSetting(shopId);
//
//        //清所有店铺设置
//       //  cleanShopListSetting(shopList);
//
//        //delete("31946c940e194311b117e3fff5327215" + "info");// （店铺设置）  shopdetailid + info
//        //(店铺设置)  (brandId+"shopList")
//    }

    private void cleanBrandSetting(String brandId){
        //1.清品牌参数设置的缓存前:
        System.out.println(("品牌参数缓存清空前"+ JsonUtils.objectToJson(redisService.get(brandId))));
        //清缓存
        redisService.remove(brandId);// brandId (品牌参数设置)
        //清缓存后
        System.out.println("清楚品牌参数缓存后:"+JsonUtils.objectToJson(redisService.get(brandId)));
    }


    private void cleanShopSetting(String shopId){
        //1.清店铺参数设置的缓存前:
        System.out.println(("店铺参数缓存清空前"+JsonUtils.objectToJson(redisService.get(shopId))));
        //清缓存
        redisService.remove(shopId);// brandId (品牌参数设置)
        //清缓存后
        System.out.println("清楚店铺参数缓存后:"+JsonUtils.objectToJson(redisService.get(shopId)));
    }

    private void cleanShopListSetting(String shopList) {
        //1.清店铺参数设置的缓存前:
        System.out.println(("店铺参数缓存清空前"+JsonUtils.objectToJson(redisService.get(shopList))));
        //清缓存
        redisService.remove(shopList);//
        //清缓存后
        System.out.println("清楚店铺参数缓存后:"+redisService.get(shopList));
    }

    private void cleanWechatSetting(String wechatId){
        //1.清品牌参数设置的缓存前:
        System.out.println(("微信参数缓存清空前"+redisService.get(wechatId)));
        //清缓存
        redisService.remove(wechatId);// brandId (品牌参数设置)
        //清缓存后
        System.out.println("微信参数参数缓存后:"+redisService.get(wechatId));
    }


}
