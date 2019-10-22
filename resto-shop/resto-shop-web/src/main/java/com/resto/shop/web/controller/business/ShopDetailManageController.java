package com.resto.shop.web.controller.business;

import com.google.zxing.WriterException;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.*;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.util.LogTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("shopDetailManage")
public class ShopDetailManageController extends GenericController {

    private static SimpleDateFormat myDate = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    ShopDetailService shopDetailService;
    @Resource
    BrandService brandService;

    @Resource
    WechatConfigService wechatConfigService;

    @Autowired
    RedisService redisService;

    @Autowired
    WeChatService weChatService;

    @RequestMapping("/list")
    public void list() {

    }


    @RequestMapping("list_one")
    @ResponseBody
    public Result list_one() {
        ShopDetail shopDetail = shopDetailService.selectById(getCurrentShopId());
        return getSuccessResult(shopDetail);
    }

    @RequestMapping("modify")
    @ResponseBody
    public Result modify(ShopDetail shopDetail) {
        shopDetail.setId(getCurrentShopId());
        log.info(shopDetail.getIsOpenTablewareFee() + "22222");
        if (shopDetail.getIsOpenSauceFee() == null) {
            shopDetail.setIsOpenSauceFee(0);
        }
        if (shopDetail.getIsOpenTablewareFee() == null) {
            shopDetail.setIsOpenTablewareFee(0);
        }
        if (shopDetail.getIsOpenTowelFee() == null) {
            shopDetail.setIsOpenTowelFee(0);
        }
//         switch (shopDetail.getWaitUnit()){
//             case 1 :
//                 shopDetail.setTimeOut(shopDetail.getWaitTime());
//                 break;
//             case 2 :
//                 shopDetail.setTimeOut(shopDetail.getWaitTime()  * 24);
//                 break;
//             case 3 :
//                 shopDetail.setTimeOut(Integer.MAX_VALUE);
//                 break;
//         }
        shopDetailService.update(shopDetail);
        if (redisService.get(getCurrentShopId() + "info") != null) {
            redisService.remove(getCurrentShopId() + "info");
        }
        Brand brand = brandService.selectByPrimaryKey(getCurrentBrandId());
        shopDetail = shopDetailService.selectByPrimaryKey(getCurrentShopId());
        LogTemplateUtils.shopDeatilEdit(brand.getBrandName(), shopDetail.getName(), getUserName());
        return Result.getSuccess();
    }

    @RequestMapping("list_all")
    @ResponseBody
    public Result listAll() {
        List<ShopDetail> lists = shopDetailService.selectByBrandId(getCurrentBrandId());
        return getSuccessResult(lists);
    }

//     @RequestMapping("/download")
//     @ResponseBody
//     public Result run(String id, HttpServletRequest request , HttpServletResponse response)
//             throws Exception {
//         ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(id);
//         Brand brand = brandService.selectById(shopDetail.getBrandId());
//
//         String photoUrl = WeChatUtils.waitRedQRcodeUrl(brand.getWechatConfig().getAppid(), brand.getWechatConfig().getAppsecret(), id);
//         String fileSavePath = getFilePath(request,null);
////         deleteFile(new File(fileSavePath));//删除历史生成的文件
//         String filepath = getFilePath(request,shopDetail.getName());
//         String localHeadPhotoPath = download(shopDetail.getName(), photoUrl, request);
//         Date data = new Date();
//         String now = myDate.format(data);
//         //打包
//         FileToZip.fileToZip(filepath, fileSavePath, ("等位二维码" + now));
//         Result result = new Result(true);
//         result.setMessage("等位二维码" + now +".zip");
//         System.gc();//手动回收垃圾，清空文件占用情况，解决无法删除文件
//         return result;
//     }

    /**
     * 得到要 保存 二维码文件夹的路径
     *
     * @param request
     * @param fileName
     * @return
     */
    public String getFilePath(HttpServletRequest request, String fileName) {
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0, lastR) + "/";
        String filePath = "qrCodeFiles/";
        if (fileName != null) {
            filePath += fileName;
        }
        return systemPath + filePath;
    }

    public static String download(String shopName, String urlString, HttpServletRequest request) throws Exception {
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0, lastR) + "/";
        systemPath = systemPath + "qrCodeFiles/" + shopName + "/";

        String localPath = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);

            InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            inStream.close();
            byte[] btImg = outStream.toByteArray();

            if (null != btImg && btImg.length > 0) {
                String fileName = shopName + "等位二维码.jpg";
                File filePath = new File(systemPath);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                File file = new File(systemPath + fileName);
                FileOutputStream fops = new FileOutputStream(file);
                fops.write(btImg);
                fops.flush();
                fops.close();
                localPath = systemPath + fileName;
            } else {
                System.out.println("没有从该连接获得内容");
            }

        } catch (Exception e) {
//			e.printStackTrace();
            System.out.println("【上传失败】");
        }
        return localPath;
    }

    public static void deleteFile(File file) {
        System.gc();//手动回收垃圾，清空文件占用情况，解决无法删除文件
        if (file.isFile() && file.exists()) {//表示该文件不是文件夹
            file.delete();
        } else if (file.isDirectory()) {
            //首先得到当前的路径
            String[] childFilePaths = file.list();
            for (String childFilePath : childFilePaths) {
                File childFile = new File(file.getAbsolutePath() + File.separator + childFilePath);
                deleteFile(childFile);
            }
            file.delete();
        }
    }

    @RequestMapping("openQRCode")
    @ResponseBody
    public String openQRCode(String shopId, HttpServletRequest request, HttpServletResponse response) throws IOException, WriterException {
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(shopId);
        WechatConfig wechatConfig = wechatConfigService.selectByBrandId(shopDetail.getBrandId());
        String token = weChatService.getAccessToken(wechatConfig.getAppid(), wechatConfig.getAppsecret());
        org.json.JSONObject qrParam = new org.json.JSONObject();
        qrParam.put("QrCodeId", shopId);
        String result = weChatService.getParamQrCode(token, qrParam.toString());//二维码的附带参数字符串类型，长度不能超过64
        org.json.JSONObject obj = new org.json.JSONObject(result);
        String img = obj.has("ticket") ? obj.getString("ticket") : "";
        String conteng = weChatService.showQrcode(img);
        return conteng;
    }
}
