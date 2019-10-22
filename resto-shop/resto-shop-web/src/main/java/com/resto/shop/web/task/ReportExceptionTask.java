package com.resto.shop.web.task;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandUser;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.BrandUserService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.service.OrderService;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 定时任务。
 */
@Component("reportExceptionTask")
public class ReportExceptionTask {

    @Autowired
    BrandUserService brandUserService;
    @Autowired
    ShopDetailService shopDetailService;

    @Autowired
    BrandService brandService;

    @Autowired
    OrderService orderService;

    static Logger log = Logger.getLogger(ReportExceptionTask.class);

    //链接前缀
    static String urlBase = "http://localhost:8086";//http://op.restoplus.cn
    //登入的url
    String loginUrl = urlBase + "/shop/branduser/login";

    String orderExceptionUrl = urlBase + "/shop/syncData/syncOrderException";
    String orderPayMentItemExceptionUrl = urlBase + "/shop/syncData/syncOrderPaymentItemException";

    //@Scheduled(cron = "0/5 * *  * * ?")   //每5秒执行一次 cron = "00 09 14 * * ?"
    //				   ss mm HH
   @Scheduled(cron = "10  45 13 * * ?")   //每天12点执行

    public void syncData() throws ClassNotFoundException, UnsupportedEncodingException {
        System.out.println("开始");

        //查询所有的品牌
        List<Brand> brandList = brandService.selectList();
        for (Brand brand : brandList) {
         if(NotmatchBrand(brand.getBrandName())){
                    //获取品牌用户
                    BrandUser brandUser = brandUserService.selectUserInfoByBrandIdAndRole(brand.getId(), 8);
                    //创建 Client 对象
                    CloseableHttpClient client = HttpClients.createDefault();
                    //设置登录参数
                    Map<String, String> parameterMap = new HashMap<>();
                    parameterMap.put("username", brandUser.getUsername());
                    parameterMap.put("password", "Vino.2018");// 527527527
                    //登录
                    System.err.println("登入品牌为"+brand.getBrandName());
                    HttpResponse loginResponse = doPostAnsc(client, loginUrl, parameterMap);

                    //得到httpResponse的状态响应码
                    int statusCode = loginResponse.getStatusLine().getStatusCode();

                    if (statusCode == 302 && statusCode != HttpStatus.SC_OK) {
                        log.info("--------------HttpClient 登录成功！");
                        Map<String, String> requestMap = new HashMap<>();
                        requestMap.put("beginDate", "2017-03-11");
                        requestMap.put("endDate", "2017-03-17");
                        requestMap.put("brandName",brand.getBrandName());
                        //循环执行 URLMap 中的链接
                        HttpResponse httpResponse = doPostAnsc(client, orderExceptionUrl, requestMap);
                        HttpResponse httpResponse2 = doPostAnsc(client, orderPayMentItemExceptionUrl, requestMap);
                        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            log.info("执行了插入异常订单的请求");
                        } else {
                            log.info("--------------HttpClient 登录失败！");
                        }
                        if(httpResponse2.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                            log.info("执行了插入异常订单项的请求");
                        }
                    }
                }
            }
   }

   //定义匹配的品牌  只查询下面定义的品牌
    private  Boolean matchBrand(String brandName){
       Boolean tag = false;
       List<String> nameList = new ArrayList<>();
        nameList.add("火宴山");
        if(nameList.contains(brandName)){
            tag=true;
        }
       return  tag;
    }

    //定义不匹配的品牌 除了以下品牌不查其它都查
    private  Boolean NotmatchBrand(String brandName){
        Boolean tag = true;
        List<String> nameList = new ArrayList<>();
        nameList.add("测试专用品牌");
        nameList.add("餐加");
        nameList.add("港都小排挡");
        nameList.add("夜狼音乐餐吧");
        nameList.add("座头市");
        if(nameList.contains(brandName)){
            tag = false;
        }
        return  tag;
    }


    /**
     * HttpClient Post 请求
     * @param client
     * @param url
     * @param parameterMap
     * @return
     */
    public HttpResponse doPostAnsc(CloseableHttpClient client,String url,Map<String,String> parameterMap){
        HttpPost httpPost = new HttpPost(url);
        //封装请求参数
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        Iterator it = parameterMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry parmEntry = (Map.Entry) it.next();
            param.add(new BasicNameValuePair((String) parmEntry.getKey(),(String) parmEntry.getValue()));
        }
        HttpResponse httpResponse = null;
        try {
            UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(param, "UTF-8");
            httpPost.setEntity(postEntity);
            httpResponse =client.execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpResponse;
    }



}