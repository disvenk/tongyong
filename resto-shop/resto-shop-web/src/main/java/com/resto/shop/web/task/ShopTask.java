//package com.resto.shop.web.task;
//
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.math.BigDecimal;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.NameValuePair;
//import org.apache.http.ParseException;
//import org.apache.http.client.CookieStore;
//import org.apache.http.client.config.CookieSpecs;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.protocol.HttpClientContext;
//import org.apache.http.config.Registry;
//import org.apache.http.config.RegistryBuilder;
//import org.apache.http.cookie.CookieSpecProvider;
//import org.apache.http.impl.client.BasicCookieStore;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.cookie.BasicClientCookie;
//import org.apache.http.impl.cookie.BestMatchSpecFactory;
//import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.resto.brand.core.util.ApplicationUtils;
//import com.resto.brand.core.util.DateUtil;
//import com.resto.brand.web.model.BrandUser;
//import com.resto.brand.web.model.ShopDetail;
//import com.resto.brand.web.service.BrandUserService;
//import com.resto.brand.web.service.ShopDetailService;
//import com.resto.shop.web.controller.GenericController;
//
///**
// * Created by JuanSheng on 2016/7/14.
// */
//@Component("shopTask")
//public class ShopTask extends GenericController {
//
//    @Autowired
//    BrandUserService brandUserService;
//    @Autowired
//    ShopDetailService shopDetailService;
//
//    //9407a95341b0440c90d920fb2cedefaf
//
//    //链接前缀
//    String urlBase = "http://localhost:8081";//http://op.restoplus.cn
//
//    //登入的url
//    String loginUrl = urlBase + "/shop/branduser/login";
//
//    //获取营业收入报表的数据的url(包括品牌数据和店铺的数据)					******
//    String incomeUrl = urlBase + "/shop/totalIncome/reportIncome";
//
//    //品牌菜品销售报表的数据的url									******
//    String brandArticleUrl = urlBase + "/shop/articleSell/brand_id_data";
//
//    //店铺菜品的销售报表的url
//    String shopArticleUrl = urlBase + "/shop/articleSell/shop_data";
//
//    //订单的url
//    String shopOrderUrl = urlBase + "/shop/orderReport/AllOrder";
//
//    //订单 菜品信息 url
//    String orderItemsUrl = urlBase + "/shop/syncData/syncOrderArticle";
//
//    //获取前一天的时间 (开始时间 和 结束时间)
//    String beginTime = DateUtil.getYesterDayBegin();
//    String endTime = DateUtil.getYesterDayEnd();
//
//    // 创建CookieStore实例
//    static CookieStore cookieStore = null;
//    static HttpClientContext context = null;
//
//    String url = "jdbc:mysql://127.0.0.1:3306/middle?useUnicode=true&characterEncoding=utf8";
//    String driver = null;
//    String username = "root";
//    String password = "root";
//    Connection con = null;
//    PreparedStatement sta = null;
//
//
////     @Scheduled(cron = "0/5 * *  * * ?")   //每5秒执行一次
//    //				   ss mm HH
//
//   // @Scheduled(cron = "00 09 14 * * ?")   //每天12点执行
//    public void job1() throws ClassNotFoundException, UnsupportedEncodingException {
//
//    	//简厨 974b0b1e31dc4b3fb0c3d9a0970d22e4
//    	//书香茶香 1386c0c0f35f466097fc770bec7d6400
//    	String brandId = "974b0b1e31dc4b3fb0c3d9a0970d22e4";
//        //获取品牌用户
//        BrandUser brandUser = brandUserService.selectUserInfoByBrandIdAndRole(brandId, 8);
//        
//        List<ShopDetail> list_shopDetail = shopDetailService.selectByBrandId(brandId);
//        
//        
//        // 直接创建client
//        CloseableHttpClient client = HttpClients.createDefault();
//        //登入的http请求
//        HttpPost httpPostLogin = new HttpPost(loginUrl);
//        //登入请求的参数
//        Map loginMap = new HashMap();
////        loginMap.put("username", "kc_admin");
////        loginMap.put("password", "c888c24ab6f0d64439f3002823f211f2fb4015cb");// 527527527
//        loginMap.put("username", brandUser.getUsername());
//        loginMap.put("password", brandUser.getPassword());// 527527527
//        loginMap.put("isMD5", "true");
//        
//        UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(
//                getParam(loginMap), "UTF-8");
//
//        httpPostLogin.setEntity(postEntity);
//
//        List<String> list = new LinkedList<>();
//        list.add(incomeUrl);
//        list.add(brandArticleUrl);
//        list.add(shopArticleUrl);
//        list.add(shopOrderUrl);
//        list.add(orderItemsUrl);
//
//        try {
//            // 执行post请求（登入操作）
//            //登入的cookie
//            HttpResponse httpResponse = client.execute(httpPostLogin);
//
//            System.out.println("----the same client---登录成功！");//同一个客户端查询
//
//            Map  map = new HashMap();
//            map.put("beginDate",beginTime);
//            map.put("endDate",endTime);
//
//            //调用方法执行http连接并插入数据
//
//            
//            for (int i = 0; i <list.size() ; i++) {
//            	if(i==0){
//            		netAndInsert(list.get(i),map,client,i);
//            		System.out.println(" incomeUrl ---- ");
//            	}else if(i==1){
//                    map.put("sort","desc");
//                    netAndInsert(list.get(i),map,client,i);
//                }else if (i==2){
////                  map.put("shopId","f3910fceb055442ab6b3abc6642eb70a");
//                    for(int n = 0; n < list_shopDetail.size() ; n++ ){
//                		ShopDetail shopDetail = list_shopDetail.get(n);
//                		map.put("shopId",shopDetail.getId());
//                        netAndInsert(list.get(i),map,client,i);
//                        System.out.println(" shopArticleUrl ----  【"+shopDetail.getBrandName()+"】    "+shopDetail.getName());
//                	}
//                }else if(i==3){
////                  map.put("shopId","f3910fceb055442ab6b3abc6642eb70a");
//	              	for(int n = 0; n < list_shopDetail.size() ; n++ ){
//	              		ShopDetail shopDetail = list_shopDetail.get(n);
//	              		map.put("shopId",shopDetail.getId());
//	                      netAndInsert(list.get(i),map,client,i);
//	                      System.out.println(" shopArticleUrl ----  【"+shopDetail.getBrandName()+"】    "+shopDetail.getName());
//	              	}
//                }else if(i==4){
//                	netAndInsert(list.get(i),map,client,i);
//                	System.out.println("----订单详情");
//                }
//            }
//
//            // cookie store
//            //setCookieStore(httpResponse);
//            // context
//            //setContext();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // 关闭流并释放资源
//                client.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println("-----所有导入完毕-------");
//        }
//    }
//
//    /**
//     * 访问指定链接，获取数据，将数据插入中间数据库
//     * @param s   请求链接
//     * @param map   请求的参数
//     * @param client
//     * @param i 用于记录是第几个链接
//     * @throws IOException
//     * @throws ClassNotFoundException
//     */
//    private void netAndInsert(String s, Map map,CloseableHttpClient client,int i) throws IOException, ClassNotFoundException {
//
//        //创建post请求对象
//        HttpPost httpPost = new HttpPost(s);
//        //封装请求参数
//        UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(
//                getParam(map), "UTF-8");
//        //设置请求参数
//        httpPost.setEntity(postEntity);
//        //执行查询之后返回的数据
//        HttpResponse httpResponse =client.execute(httpPost);
//        //得到httpResponse的状态响应码
//        int statusCode=httpResponse.getStatusLine().getStatusCode();
//
//        //插入的结果
//        String result = "";
//        if (statusCode== HttpStatus.SC_OK) {
//            result = getResult(httpResponse);
//        }
//
//        //与驱动做连接
//
//        driver = "com.mysql.jdbc.Driver";
//        Class.forName(driver);
//
//        try {
//            con = DriverManager.getConnection(url, username, password);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        String sign = "";//用来判断是要当前插入的是哪张表
//        if(i==0){
//            //获取店铺营业额的数据
//            JSONObject resultJsonObject = new JSONObject(result);
//            JSONArray jsonArray=resultJsonObject.getJSONArray("shopIncome");
//            sign = "shopIncome";
//            doSomethigReday(jsonArray,sign);
//
//            //获取品牌营业额数据
//            JSONArray jsonArray2=resultJsonObject.getJSONArray("brandIncome");
//            sign = "brandIncome";
//            doSomethigReday(jsonArray2,sign);
//        }else if (i==1){//品牌菜品的数据
//            JSONObject resultJsonObject = new JSONObject(result);
//            JSONArray jsonArray=resultJsonObject.getJSONArray("data");
//            sign = "brandArticleSell";
//            doSomethigReday(jsonArray,sign);
//        }else if (i==2){//店铺菜品的数据
//            JSONObject resultJsonObject = new JSONObject(result);
//            JSONArray jsonArray=resultJsonObject.getJSONArray("data");
//            sign = "shopArticleSell";
//            doSomethigReday(jsonArray,sign);
//        }else if(i==3){
//            sign = "allOrder";
//            JSONArray jsonArray = new JSONArray(result);
//            doSomethigReday(jsonArray,sign);
//        }else if(i==4){
//        	sign = "orderItems";
//        	JSONObject resultJsonObject = new JSONObject(result);
//            JSONArray jsonArray=resultJsonObject.getJSONArray("data");
//            doSomethigReday(jsonArray,sign);
//        }
//
//    }
//
//    private void doSomethigReday(JSONArray jsonArray,String sign) {
//
//        if("shopIncome".equals(sign)){
//            for (int j = 0; j < jsonArray.length(); j++) {
//                JSONObject everyJsonObject=jsonArray.getJSONObject(j);
//                String id= ApplicationUtils.randomUUID();//生成随机的id
//                String shop_id = everyJsonObject.getString("shopDetailId");
//                String shop_name = everyJsonObject.getString("shopName");
//                BigDecimal total_income = everyJsonObject.getBigDecimal("totalIncome");
//                BigDecimal wechat_pay = everyJsonObject.getBigDecimal("wechatIncome") ;
//                BigDecimal charge_pay = everyJsonObject.getBigDecimal("chargeAccountIncome") ;
//                BigDecimal red_packet_pay = everyJsonObject.getBigDecimal("redIncome") ;
//                BigDecimal coupon_pay = everyJsonObject.getBigDecimal("couponIncome") ;
//                BigDecimal charge_reward_pay = everyJsonObject.getBigDecimal("chargeGifAccountIncome");
////                String sql =  "insert into shop_income(id,shop_id,shop_name,total_income,wechat_pay,charge_pay,red_packet_pay,coupon_pay,charge_reward_pay,create_time,end_time) values(?,?,?,?,?,?,?,?,?,?,?)";
//                String sql =  "insert into shop_income(id,shop_id,shop_name,total_income,wechat_pay,charge_pay,red_packet_pay,coupon_pay,charge_reward_pay,data_time) values(?,?,?,?,?,?,?,?,?,?)";
//                Map<String,Object> map = new HashMap<>();
//                map.put("id",id);
//                map.put("shop_id",shop_id);
//                map.put("shop_name",shop_name);
//                map.put("total_income",total_income!=null?total_income:0);
//                map.put("wechat_pay",wechat_pay);
//                map.put("charge_pay",charge_pay);
//                map.put("red_packet_pay",red_packet_pay);
//                map.put("coupon_pay",coupon_pay);
//                map.put("charge_reward_pay",charge_reward_pay);
////                map.put("create_time",beginTime);
////                map.put("end_time",endTime);
//                map.put("key","shopIncome");//标识插入到哪个表
//                doInsert(sql,con,map);
//            }
//            System.out.println("店铺收入数据导入成功");
//
//        }else if("brandIncome".equals(sign)){
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject everyJsonObject=jsonArray.getJSONObject(i);
//                String id= ApplicationUtils.randomUUID();//生成随机的id
//                String brand_id = everyJsonObject.getString("brandId");
//                String brand_name = everyJsonObject.getString("brandName");
//                BigDecimal total_income = everyJsonObject.getBigDecimal("totalIncome");
//                BigDecimal wechat_pay = everyJsonObject.getBigDecimal("wechatIncome") ;
//                BigDecimal charge_pay = everyJsonObject.getBigDecimal("chargeAccountIncome") ;
//                BigDecimal red_packet_pay = everyJsonObject.getBigDecimal("redIncome") ;
//                BigDecimal coupon_pay = everyJsonObject.getBigDecimal("couponIncome") ;
//                BigDecimal charge_reward_pay = everyJsonObject.getBigDecimal("chargeGifAccountIncome");
////                String sql =  "insert into brand_income(id,brand_id,brand_name,total_income,wechat_pay,charge_pay,red_packet_pay,coupon_pay,charge_reward_pay,create_time,end_time) values(?,?,?,?,?,?,?,?,?,?,?)";
//                String sql =  "insert into brand_income(id,brand_id,brand_name,total_income,wechat_pay,charge_pay,red_packet_pay,coupon_pay,charge_reward_pay,data_time) values(?,?,?,?,?,?,?,?,?,?)";
//                Map<String,Object> map = new HashMap<>();
//                map.put("id",id);
//                map.put("brand_id",brand_id);
//                map.put("brand_name",brand_name);
//                map.put("total_income",total_income);
//                map.put("wechat_pay",wechat_pay);
//                map.put("charge_pay",charge_pay);
//                map.put("red_packet_pay",red_packet_pay);
//                map.put("coupon_pay",coupon_pay);
//                map.put("charge_reward_pay",charge_reward_pay);
////                map.put("create_time",beginTime);
////                map.put("end_time",endTime);
//                map.put("key","brandIncome");
//                //执行插入营业额的报表
//                doInsert(sql,con,map);
//            }
//            System.out.println("品牌收入数据导入成功");
//        }else if("brandArticleSell".equals(sign)){
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject everyJsonObject=jsonArray.getJSONObject(i);
//                String id= ApplicationUtils.randomUUID();//生成随机的id
//                String shop_id = everyJsonObject.getString("shopId");
//                String article_id = everyJsonObject.getString("articleId");
//                String article_family_name = everyJsonObject.getString("articleFamilyName");
//                String article_name = everyJsonObject.getString("articleName");
//                int sales = everyJsonObject.getInt("brandSellNum") ;//销量
//                String sales_occupies = everyJsonObject.getString("numRatio") ;//销量占比
//                BigDecimal sell = everyJsonObject.getBigDecimal("salles") ;//销售
//                String sell_occupies = everyJsonObject.getString("salesRatio") ;//销售占比
////                String sql =  "insert into brand_article(id,article_id,article_family_name,article_name,salles,salles_occupies,sell,sell_occupies,create_time,end_time,shop_id) values(?,?,?,?,?,?,?,?,?,?,?)";
//                String sql =  "insert into brand_article(id,article_id,article_family_name,article_name,salles,salles_occupies,sell,sell_occupies,data_time,shop_id) values(?,?,?,?,?,?,?,?,?,?)";
//                Map<String,Object> map = new HashMap<>();
//                map.put("id",id);
//                map.put("article_id",article_id);
//                map.put("article_family_name",article_family_name);
//                map.put("article_name",article_name);
//                map.put("sales",sales);
//                map.put("sales_occupies",sales_occupies);
//                map.put("sell",sell);
//                map.put("sell_occupies",sell_occupies);
////                map.put("create_time",beginTime);
////                map.put("end_time",endTime);
//                map.put("shop_id",shop_id);
//                map.put("key","brandArticleSell");
//                //执行插入营业额的报表
//                doInsert(sql,con,map);
//            }
//            System.out.println("品牌菜品数据导入成功");
//
//        }else if ("shopArticleSell".equals(sign)){
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject everyJsonObject=jsonArray.getJSONObject(i);
//                String id= ApplicationUtils.randomUUID();//生成随机的id
//                String article_id = everyJsonObject.getString("articleId");
//                String article_family_name = everyJsonObject.getString("articleFamilyName");
//                String article_name = everyJsonObject.getString("articleName");
//                int sales = everyJsonObject.getInt("shopSellNum") ;//销量
//                String sales_occupies = everyJsonObject.getString("numRatio") ;//销量占比
//                BigDecimal sell = everyJsonObject.getBigDecimal("salles") ;//销售
//                String sell_occupies = everyJsonObject.getString("salesRatio") ;//销售占比
//                String shop_id = everyJsonObject.getString("shopId") ;//店铺ID
////                String sql =  "insert into shop_article(id,article_id,article_family_name,article_name,salles,salles_occupies,sell,sell_occupies,create_time,end_time,shop_id) values(?,?,?,?,?,?,?,?,?,?,?)";
//                String sql =  "insert into shop_article(id,article_id,article_family_name,article_name,salles,salles_occupies,sell,sell_occupies,data_time,shop_id) values(?,?,?,?,?,?,?,?,?,?)";
//                Map<String,Object> map = new HashMap<>();
//                map.put("id",id);
//                map.put("article_id",article_id);
//                map.put("article_family_name",article_family_name);
//                map.put("article_name",article_name);
//                map.put("sales",sales);
//                map.put("sales_occupies",sales_occupies);
//                map.put("sell",sell);
//                map.put("sell_occupies",sell_occupies);
////                map.put("create_time",beginTime);
////                map.put("end_time",endTime);
//                map.put("shop_id", shop_id);
//                map.put("key","shopArticleSell");
//                doInsert(sql,con,map);
//            }
//            System.out.println("店铺菜品数据导入成功");
//        }
//        else if("allOrder".equals(sign)){
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject everyJsonObject=jsonArray.getJSONObject(i);
//                String id= ApplicationUtils.randomUUID();//生成随机的id
//                String shop_name = everyJsonObject.getString("shopName");
//                String shop_id = everyJsonObject.getString("shopDetailId");
//                String order_id = everyJsonObject.getString("orderId");
//                String order_time = everyJsonObject.getString("begin");//下单时间
//                String telephone = everyJsonObject.getString("telephone") ;//
//                BigDecimal order_money = everyJsonObject.getBigDecimal("orderMoney") ;
//                BigDecimal wechat_pay = everyJsonObject.getBigDecimal("weChatPay") ;
//                BigDecimal charge_pay = everyJsonObject.getBigDecimal("chargePay") ;
//                BigDecimal charge_reward_pay = everyJsonObject.getBigDecimal("rewardPay");
//                BigDecimal red_packet = everyJsonObject.getBigDecimal("accountPay");
//                BigDecimal coupon_pay = everyJsonObject.getBigDecimal("couponPay");
//                String level2 = everyJsonObject.getString("level") ;
//                String order_state2 = everyJsonObject.getString("orderState");
//                int level = getLevel(level2);
//                int order_state = getOrderState(order_state2);
////                String sql = "insert into order_detail(id,shop_name,order_id,order_time,telephone,order_money," +
////                        "wechat_pay," +"charge_pay,charge_reward_pay,red_packet_pay,coupon_pay,level,order_state," +
////                        "create_time,end_time,shop_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//                String sql = "insert into order_detail(id,shop_name,order_id,order_time,telephone,order_money," +
//                        "wechat_pay," +"charge_pay,charge_reward_pay,red_packet_pay,coupon_pay,level,order_state," +
//                        "data_time,shop_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//                Map<String,Object> map = new HashMap<>();
//                map.put("id",id);
//                map.put("shop_name",shop_name);
//                map.put("order_id",order_id);
//                map.put("order_time",order_time);
//                map.put("telephone",telephone);
//                map.put("order_money",order_money);
//                map.put("wechat_pay",wechat_pay);
//                map.put("charge_pay",charge_pay);
//                map.put("charge_reward_pay",charge_reward_pay);
//                map.put("red_packet",red_packet);
//                map.put("coupon_pay",coupon_pay);
//                map.put("level",level);
//                map.put("order_state",order_state);
////                map.put("create_time",beginTime);
////                map.put("end_time",endTime);
//                map.put("shop_id",shop_id);
//                map.put("key","allOrder");
//                doInsert(sql,con,map);
//            }
//            System.out.println("订单数据导入成功");
//        }else if("orderItems".equals(sign)){
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject everyJsonObject=jsonArray.getJSONObject(i);
//                String id= ApplicationUtils.randomUUID();//生成随机的id
//                String shop_id = everyJsonObject.getString("shop_id");//店铺ID
//                String order_id = everyJsonObject.getString("order_id") ;//订单编号
//                String order_time = everyJsonObject.getString("order_time");//下单时间
//                String telephone = everyJsonObject.getString("telephone") ;//电话
//                String article_name = everyJsonObject.getString("article_name") ;//菜品名称
//                int count = everyJsonObject.getInt("article_num") ;//数量
////                String sql = "insert into order_article(id,shop_id,order_id,order_time,telephone,article_name,article_num) values(?,?,?,?,?,?,?)";
//                String sql = "insert into order_article(id,shop_id,order_id,order_time,telephone,article_name,article_num,data_time) values(?,?,?,?,?,?,?,?)";
//                Map<String,Object> map = new HashMap<>();
//                map.put("id",id);
//                map.put("shop_id",shop_id);
//                map.put("order_id",order_id);
//                map.put("order_time",order_time);
//                map.put("telephone",telephone);
//                map.put("article_name",article_name);
//                map.put("article_num",count);
//                map.put("key","orderItems");
//                doInsert(sql,con,map);
//            }
//            System.out.println("订单详情导入成功");
//        }
//    }
//
//
//    private void doInsert(String sql,Connection con,Map<String,Object> map) {
//        try {
//            sta = con.prepareStatement(sql);
//
//            //判断是插入哪个数据库
//            if("shopIncome".equals(map.get("key").toString())){
//                //插入数据
//                sta.setString(1,map.get("id").toString());
//                sta.setString(2,map.get("shop_id").toString());
//                sta.setString(3,map.get("shop_name").toString());
//                sta.setBigDecimal(4,new BigDecimal(map.get("total_income").toString()));
//                sta.setBigDecimal(5,new BigDecimal(map.get("wechat_pay").toString()));
//                sta.setBigDecimal(6,new BigDecimal(map.get("charge_pay").toString()));
//                sta.setBigDecimal(7,new BigDecimal(map.get("red_packet_pay").toString()));
//                sta.setBigDecimal(8,new BigDecimal(map.get("coupon_pay").toString()));
//                sta.setBigDecimal(9,new BigDecimal(map.get("charge_reward_pay").toString()));
////                sta.setString(10,map.get("create_time").toString());
////                sta.setString(11,map.get("end_time").toString());
//                sta.setString(10,DateUtil.getYesterDay());
//                sta.executeUpdate();
//            }else if("brandIncome".equals(map.get("key").toString())){
//                //插入数据
//                sta.setString(1,map.get("id").toString());
//                sta.setString(2,map.get("brand_id").toString());
//                sta.setString(3,map.get("brand_name").toString());
//                sta.setBigDecimal(4,new BigDecimal(map.get("total_income").toString()));
//                sta.setBigDecimal(5,new BigDecimal(map.get("wechat_pay").toString()));
//                sta.setBigDecimal(6,new BigDecimal(map.get("charge_pay").toString()));
//                sta.setBigDecimal(7,new BigDecimal(map.get("red_packet_pay").toString()));
//                sta.setBigDecimal(8,new BigDecimal(map.get("coupon_pay").toString()));
//                sta.setBigDecimal(9,new BigDecimal(map.get("charge_reward_pay").toString()));
////                sta.setString(10,map.get("create_time").toString());
////                sta.setString(11,map.get("end_time").toString());
//                sta.setString(10,DateUtil.getYesterDay());
//                sta.executeUpdate();
//            }else if("brandArticleSell".equals(map.get("key").toString())){
//                //插入数据
//                sta.setString(1,map.get("id").toString());
//                sta.setString(2,map.get("article_id").toString());
//                sta.setString(3,map.get("article_family_name").toString());
//                sta.setString(4,map.get("article_name").toString());
//                sta.setString(5,map.get("sales").toString());
//                sta.setString(6,map.get("sales_occupies").toString());
//                sta.setString(7,map.get("sell").toString());
//                sta.setString(8,map.get("sell_occupies").toString());
////                sta.setString(9,map.get("create_time").toString());
////                sta.setString(10,map.get("end_time").toString());
//                sta.setString(9,DateUtil.getYesterDay());
////                sta.setString(11,map.get("shop_id").toString());
//                sta.setString(10,map.get("shop_id").toString());
//                sta.executeUpdate();
//            }else if ("shopArticleSell".equals(map.get("key").toString())){
//                sta.setString(1,map.get("id").toString());
//                sta.setString(2,map.get("article_id").toString());
//                sta.setString(3,map.get("article_family_name").toString());
//                sta.setString(4,map.get("article_name").toString());
//                sta.setString(5,map.get("sales").toString());
//                sta.setString(6,map.get("sales_occupies").toString());
//                sta.setString(7,map.get("sell").toString());
//                sta.setString(8,map.get("sell_occupies").toString());
////                sta.setString(9,map.get("create_time").toString());
////                sta.setString(10,map.get("end_time").toString());
//                sta.setString(9,DateUtil.getYesterDay());
////                sta.setString(11,map.get("shop_id").toString());
//                sta.setString(10,map.get("shop_id").toString());
//                sta.executeUpdate();
//            }else if("allOrder".equals(map.get("key").toString())){
//                sta.setString(1,map.get("id").toString());
//                sta.setString(2,map.get("shop_name").toString());
//                sta.setString(3,map.get("order_id").toString());
//                sta.setString(4,map.get("order_time").toString());
//                sta.setString(5,map.get("telephone").toString());
//                sta.setString(6,map.get("order_money").toString());
//                sta.setString(7,map.get("wechat_pay").toString());
//                sta.setString(8,map.get("charge_pay").toString());
//                sta.setString(9,map.get("charge_reward_pay").toString());
//                sta.setString(10,map.get("red_packet").toString());
//                sta.setString(11,map.get("coupon_pay").toString());
//                sta.setString(12,map.get("level").toString());
//                sta.setString(13,map.get("order_state").toString());
////                sta.setString(14,map.get("create_time").toString());
////                sta.setString(15,map.get("end_time").toString());
//                sta.setString(14,DateUtil.getYesterDay());
////                sta.setString(16,map.get("shop_id").toString());
//                sta.setString(15,map.get("shop_id").toString());
//                sta.executeUpdate();
//            }else if("orderItems".equals(map.get("key").toString())){
//            	sta.setString(1,map.get("id").toString());
//                sta.setString(2,map.get("shop_id").toString());
//                sta.setString(3,map.get("order_id").toString());
//                sta.setString(4,map.get("order_time").toString());
//                sta.setString(5,map.get("telephone").toString());
//                sta.setString(6,map.get("article_name").toString());
//                sta.setString(7,map.get("article_num").toString());
//                sta.setString(8,DateUtil.getYesterDay());
//                sta.executeUpdate();
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//
//        }
//
//    }
//
//
//    //
//    public static List<NameValuePair> getParam(Map parameterMap) {
//        List<NameValuePair> param = new ArrayList<NameValuePair>();
//        Iterator it = parameterMap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry parmEntry = (Map.Entry) it.next();
//            param.add(new BasicNameValuePair((String) parmEntry.getKey(),
//                    (String) parmEntry.getValue()));
//        }
//        return param;
//    }
//
//
//    private int getOrderState(String os) {
//        int i = 0;
//
//        if("已支付".equals(os)){
//            i=2;
//        }else if("已取消".equals(os)){
//            i=9;
//        }else  if("已确认".equals(os)){
//            i=10;
//        }else if("已消费".equals(os)){
//            i=11;
//        }else if("已评价".equals(os)){
//            i=12;
//        }else if("已分享".equals(os)){
//            i=13;
//        }
//        return i;
//
//    }
//
//    private int getLevel(String os) {
//        int i = 0;
//
//        if("一星".equals(os)){
//            i=1;
//        }else if("二星".equals(os)){
//            i=2;
//        }else  if("三星".equals(os)){
//            i=3;
//        }else if("四星".equals(os)){
//            i=4;
//        }else if("五星".equals(os)){
//            i=5;
//        }
//        return i;
//    }
//
//
//    //获取response里的数据
//    public static  String getResult(HttpResponse httpResponse)throws ParseException, IOException{
//        // 获取响应消息实体
//        HttpEntity entity = httpResponse.getEntity();
//        // 判断响应实体是否为空
//        String responseString = "";
//        if (entity != null) {
//            responseString = EntityUtils.toString(entity).replace("\r\n", "");
//        }
//
//        return responseString;
//    }
//
//
//    public static void setContext() {
//        System.out.println("----setContext");
//        context = HttpClientContext.create();
//        Registry<CookieSpecProvider> registry = RegistryBuilder
//                .<CookieSpecProvider> create()
//                .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
//                .register(CookieSpecs.BROWSER_COMPATIBILITY,
//                        new BrowserCompatSpecFactory()).build();
//        context.setCookieSpecRegistry(registry);
//        context.setCookieStore(cookieStore);
//    }
//
//    public static void setCookieStore(HttpResponse httpResponse) {
//        System.out.println("----setCookieStore");
//        cookieStore = new BasicCookieStore();
//        // JSESSIONID
//        String setCookie = httpResponse.getFirstHeader("Set-Cookie")
//                .getValue();
//        String JSESSIONID = setCookie.substring("JSESSIONID=".length(),
//                setCookie.indexOf(";"));
//        System.out.println("JSESSIONID:" + JSESSIONID);
//        // 新建一个Cookie
//        BasicClientCookie cookie = new BasicClientCookie("JSESSIONID",
//                JSESSIONID);
//        cookie.setVersion(0);
//        cookie.setDomain("127.0.0.1");
//        cookie.setPath("/CwlProClient");
//        // cookie.setAttribute(ClientCookie.VERSION_ATTR, "0");
//        // cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "127.0.0.1");
//        // cookie.setAttribute(ClientCookie.PORT_ATTR, "8080");
//        // cookie.setAttribute(ClientCookie.PATH_ATTR, "/CwlProWeb");
//        cookieStore.addCookie(cookie);
//    }
//
//
//    // 关闭相关的对象
//    public void close(Connection con, PreparedStatement st, ResultSet rs) {
//        try {
//            if (rs != null) {
//                rs.close();
//            }
//            if (st != null) {
//                st.close();
//            }
//            if (con != null) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
