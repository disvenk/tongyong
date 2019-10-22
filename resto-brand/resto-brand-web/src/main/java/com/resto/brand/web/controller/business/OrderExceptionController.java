package com.resto.brand.web.controller.business;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandUser;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.BrandUserService;
import com.resto.brand.web.service.ShopDetailService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.OrderException;
import com.resto.brand.web.service.OrderExceptionService;

@Controller
@RequestMapping("orderexception")
public class OrderExceptionController extends GenericController{

	@Resource
	OrderExceptionService orderexceptionService;

    @Autowired
    BrandUserService brandUserService;

    @Autowired
    ShopDetailService shopDetailService;

    @Autowired
    BrandService brandService;

    static Logger log = Logger.getLogger(OrderExceptionController.class);

    //链接前缀
    static String urlBase = "http://localhost:8086";//http://op.restoplus.cn
    //登入的url
    String loginUrl = urlBase + "/shop/branduser/login";

    String orderExceptionUrl = urlBase + "/shop/syncData/syncOrderException";
    String orderPayMentItemExceptionUrl = urlBase + "/shop/syncData/syncOrderPaymentItemException";

	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<OrderException> listData(){
		return orderexceptionService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		OrderException orderexception = orderexceptionService.selectById(id);
		return getSuccessResult(orderexception);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid OrderException brand){
		orderexceptionService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid OrderException brand){
		orderexceptionService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		orderexceptionService.delete(id);
		return Result.getSuccess();
	}

	@RequestMapping("doExcetpionOrder")
    @ResponseBody
    public  Result doExcetpionOrder(String beginDate ,String endDate) throws UnsupportedEncodingException, ClassNotFoundException {
        this.syncData();
        return  Result.getSuccess();
    }


    public void syncData() throws ClassNotFoundException, UnsupportedEncodingException {
        System.out.println("开始");
        //查询所有的品牌
        List<Brand> brandList = brandService.selectList();
        for (Brand brand : brandList) {
            //获取品牌用户
            BrandUser brandUser = brandUserService.selectUserInfoByBrandIdAndRole(brand.getId(), 8);
            //创建 Client 对象
            CloseableHttpClient client = HttpClients.createDefault();
            //设置登录参数
            Map<String, String> parameterMap = new HashMap<>();
            parameterMap.put("username", brandUser.getUsername());
            parameterMap.put("password", brandUser.getPassword());// 527527527
            parameterMap.put("isMD5", "true");
            //登录
            HttpResponse loginResponse = doPost(client, loginUrl, parameterMap);
            //设置跨域访问
            loginResponse.setHeader("Access-Control-Allow-Origin", "*");
            loginResponse.setHeader("Access-Control-Allow-Methods", "POST");
            loginResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

            //得到httpResponse的状态响应码
            int statusCode = loginResponse.getStatusLine().getStatusCode();

            if (statusCode == 302 && statusCode != HttpStatus.SC_OK) {
                log.info("--------------HttpClient 登录成功！");
                Map<String, String> requestMap = new HashMap<>();
                requestMap.put("beginDate", DateUtil.getYesterDay());
                requestMap.put("endDate", DateUtil.getYesterDay());
                requestMap.put("brandName",brand.getBrandName());
                //循环执行 URLMap 中的链接

                HttpResponse httpResponse = doPost(client, orderExceptionUrl, requestMap);
                //设置跨域访问
                httpResponse.setHeader("Access-Control-Allow-Origin", "*");
                httpResponse.setHeader("Access-Control-Allow-Methods", "POST");
                httpResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

                HttpResponse httpResponse2 = doPost(client, orderPayMentItemExceptionUrl, requestMap);
                httpResponse2.setHeader("Access-Control-Allow-Origin", "*");
                httpResponse2.setHeader("Access-Control-Allow-Methods", "POST");
                httpResponse2.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");

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

    /**
         * HttpClient Post 请求
         * @param client
         * @param url
         * @param parameterMap
         * @return
         */
    public HttpResponse doPost(CloseableHttpClient client,String url,Map<String,String> parameterMap){
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





