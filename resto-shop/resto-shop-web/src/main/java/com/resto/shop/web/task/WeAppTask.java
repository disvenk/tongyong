//package com.resto.shop.web.task;
//
//import com.resto.brand.web.model.Brand;
//import com.resto.brand.web.model.BrandUser;
//import com.resto.brand.web.service.BrandService;
//import com.resto.brand.web.service.BrandUserService;
//import com.resto.brand.web.service.ShopDetailService;
//import com.resto.shop.web.service.OrderService;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.*;
//
///**
// * Created by yz on 2017-01-17.
// */
//
///**
// * 定时任务。
// */
//@Component("weAppTask")
//public class WeAppTask {
//
//    @Autowired
//    BrandUserService brandUserService;
//    @Autowired
//    ShopDetailService shopDetailService;
//
//    @Autowired
//    BrandService brandService;
//
//    @Autowired
//    OrderService orderService;
//
//    static Logger log = Logger.getLogger(WeAppTask.class);
//
//    //链接前缀
//    static String urlBase = "http://localhost:8086";//http://op.restoplus.cn
//    //登入的url
//    String loginUrl = urlBase + "/shop/branduser/login";
//    //小程序需要的数据
//    String weUrl = urlBase+"/shop/syncData/getWeAppInfo";
//
//    //@Scheduled(cron = "00 51 14 * * ?")   //每天12点执行
//   @Scheduled(cron = "0/5 * *  * * ?")   //每5秒执行一次 cron = "00 09 14 * * ?"
//    public void syncData() throws ClassNotFoundException, UnsupportedEncodingException {
//        System.out.println("开始");
//        //查询所有的品牌
//        List<Brand> brandList = brandService.selectList();
//        for (Brand brand : brandList) {
//                log.debug("当前品牌为:"+brand.getBrandName());
//                //获取品牌用户
//                BrandUser brandUser = brandUserService.selectUserInfoByBrandIdAndRole(brand.getId(), 8);
//                //创建 Client 对象
//                CloseableHttpClient client = HttpClients.createDefault();
//                //设置登录参数
//                Map<String, String> parameterMap = new HashMap<>();
//                parameterMap.put("username", brandUser.getUsername());
//                parameterMap.put("password", "Vino.2018");// 527527527
//                //登录
//                System.err.println("登入品牌为"+brand.getBrandName());
//                HttpResponse loginResponse = doPostAnsc(client, loginUrl, parameterMap);
//                //得到httpResponse的状态响应码
//                int statusCode = loginResponse.getStatusLine().getStatusCode();
//                if (statusCode == 302 && statusCode != HttpStatus.SC_OK) {
//                    log.debug("-----------登入！"+brand.getBrandName()+"成功");
//                    Map<String, String> requestMap = new HashMap<>();
//                    requestMap.put("brandName",brand.getBrandName());
//                    //循环执行 URLMap 中的链接
//                   HttpResponse httpResponse2 = doPostAnsc(client, weUrl, requestMap);
//                    if (httpResponse2.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                        log.info("执行了插入小程序的数据");
//                    } else {
//                        log.info("--------------HttpClient 登录失败！");
//                    }
//
//                }
//            }
//    }
//
//
//    /**
//     * HttpClient Post 请求
//     * @param client
//     * @param url
//     * @param parameterMap
//     * @return
//     */
//    public HttpResponse doPostAnsc(CloseableHttpClient client,String url,Map<String,String> parameterMap){
//        HttpPost httpPost = new HttpPost(url);
//        //封装请求参数
//        List<NameValuePair> param = new ArrayList<NameValuePair>();
//        Iterator it = parameterMap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry parmEntry = (Map.Entry) it.next();
//            param.add(new BasicNameValuePair((String) parmEntry.getKey(),(String) parmEntry.getValue()));
//        }
//        HttpResponse httpResponse = null;
//        try {
//            UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(param, "UTF-8");
//            httpPost.setEntity(postEntity);
//            httpResponse =client.execute(httpPost);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return httpResponse;
//    }
//}
