package com.resto.shop.web.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.model.BrandUser;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandUserService;
import com.resto.brand.web.service.ShopDetailService;

/**
 * 定时任务。 用于同步 中间数据库
 * 在每天 凌晨两点半   同步前一天的 报表数据
 * @author lmx
 *
 */
@Component("reportDataTask")
public class ReportDataTask{

	@Autowired
    BrandUserService brandUserService;
    @Autowired
    ShopDetailService shopDetailService;

    static Logger log = Logger.getLogger(ReportDataTask.class);

    //链接前缀
    static String urlBase = "http://localhost:8081";//http://op.restoplus.cn
    //登入的url
    String loginUrl = urlBase + "/shop/branduser/login";

    //获取数据的URL
    static Map<String, String> urlMap = new HashMap<>();

    //数据库 参数
    String url = "jdbc:mysql://127.0.0.1:3306/middle?useUnicode=true&characterEncoding=utf8";
    String driver = "com.mysql.jdbc.Driver";
    String username = "root";
    String password = "root";

    Connection conn = null;
	Statement state = null;

    static {
		//注册驱动类
		try {
		     Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		     log.error("#ERROR# :加载数据库驱动异常，请检查！", e);
		}

		urlMap.put("brand_income", urlBase + "/shop/syncData/syncBrandIncome");//品牌 总收入
		urlMap.put("shop_income", urlBase + "/shop/syncData/syncShopIncome");//店铺 总收入
		urlMap.put("brand_article", urlBase + "/shop/syncData/syncBrandOrderArticle");//品牌 菜品 销售
		urlMap.put("shop_article", urlBase + "/shop/syncData/syncShopOrderArticle");//店铺 菜品 销售
		urlMap.put("order_detail", urlBase + "/shop/syncData/syncOrderDetail");//订单 详情 信息
		urlMap.put("order_article", urlBase + "/shop/syncData/syncOrderArticle");//订单 菜品 信息
    }


//    @Scheduled(cron = "0/5 * *  * * ?")   //每5秒执行一次
    //				   ss mm HH
  // @Scheduled(cron = "00 29 13 * * ?")   //每天12点执行
    public void syncData() throws ClassNotFoundException, UnsupportedEncodingException {

    	//简厨 974b0b1e31dc4b3fb0c3d9a0970d22e4
    	//书香茶香 1386c0c0f35f466097fc770bec7d6400
    	String brandId = "974b0b1e31dc4b3fb0c3d9a0970d22e4";
        //获取品牌用户
        BrandUser brandUser = brandUserService.selectUserInfoByBrandIdAndRole(brandId, 8);
        List<ShopDetail> list_shopDetail = shopDetailService.selectByBrandId(brandId);

        //创建 Client 对象
        CloseableHttpClient client = HttpClients.createDefault();
        //设置登录参数
        Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("username", brandUser.getUsername());
        parameterMap.put("password", brandUser.getPassword());// 527527527
        parameterMap.put("isMD5", "true");
        //登录
        HttpResponse loginResponse = doPostAnsc(client, loginUrl, parameterMap);

        //得到httpResponse的状态响应码
        int statusCode = loginResponse.getStatusLine().getStatusCode();

        if (statusCode == 302 && statusCode != HttpStatus.SC_OK) {//登录成功后会 进行 重定向  页面跳转，返回的  statusCode 为 302，正常访问 密码错误时，返回的是 200.【HttpStatus.SC_OK=200】
        	log.info("--------------HttpClient 登录成功！");
    		conn = getConnection();
        	try {
				conn.setAutoCommit(false);//关闭自动提交事物
				state = conn.createStatement();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}

        	Map<String,String> requestMap = new HashMap<>();
        	requestMap.put("beginDate",DateUtil.getYesterDay());
        	requestMap.put("endDate",DateUtil.getYesterDay());

        	//循环执行 URLMap 中的链接
        	for (String key : urlMap.keySet()) {
        		HttpResponse httpResponse = doPostAnsc(client, urlMap.get(key), requestMap);
            	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    try {
    	        		List<String> sqlList = createSQL(httpResponse, key);
    	        		executeBatchSQL(state, sqlList);
    	        		conn.commit();
    				} catch (SQLException e) {
    					log.error("【"+key+"】表  -------------- 数据  插入失败！   ");
    					e.printStackTrace();
    					try {
    						conn.rollback();
    					} catch (SQLException e1) {
    						e1.printStackTrace();
    					}
    				}
                    log.info("【"+key+"】表 --------------导入完成");
            	}else{
                	log.info("查询--------------操作失败！");
            	}
        	}
        	//关闭链接
        	close(conn, state, null);
        	log.info("---------------------------------------【操作完成，关闭链接】-----------------------------------------");
        }else{
        	log.info("--------------HttpClient 登录失败！");
        }
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


    /**
     * 获取response里的数据
     * @param httpResponse
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static  String getResult(HttpResponse httpResponse){
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 判断响应实体是否为空
        String responseString = "";
        if (entity != null) {
            try {
				responseString = EntityUtils.toString(entity).replace("\r\n", "");
			} catch (ParseException | IOException e) {
				e.printStackTrace();
			}
        }
        return responseString;
    }

    /**
     * 拼接 SQL 语句
     * @param httpResponse
     * @param tableName
     */
    public List<String> createSQL(HttpResponse httpResponse,String tableName){
    	String resultData = getResult(httpResponse);
		JSONObject resultJsonObject = new JSONObject(resultData);
        JSONArray jsonArray = resultJsonObject.getJSONArray("data");
        Iterator<Object> it_data = jsonArray.iterator();
        List<String> sqlList = new ArrayList<>();
        while (it_data.hasNext()) {
        	StringBuffer sql_parameters = new StringBuffer("id,data_time,");
        	StringBuffer sql_values = new StringBuffer("'"+ApplicationUtils.randomUUID()+"',");
        	sql_values.append("'"+DateUtil.getYesterDay()+"',");
			JSONObject ob = (JSONObject) it_data.next();
			Iterator it_map = ob.keys();
			while (it_map.hasNext()) {
				String key = (String) it_map.next();
				sql_parameters.append(key+",");
				sql_values.append("'"+ob.get(key)+"',");
			}
			String parameters = sql_parameters.substring(0, sql_parameters.lastIndexOf(",")) ;
	        String values = sql_values.substring(0, sql_values.lastIndexOf(",")) ;
	        String sql = "insert into "+tableName+"("+parameters+") values("+values+")";
//	        System.out.println(sql);
	        sqlList.add(sql);
        }
        return sqlList;
    }



    
    
    /**
     * 执行 SQL
     * @param httpResponse
     * @param tableName
     */
    public void executeSQL(HttpResponse httpResponse,String tableName){
    	String resultData = getResult(httpResponse);
		JSONObject resultJsonObject = new JSONObject(resultData);
        JSONArray jsonArray = resultJsonObject.getJSONArray("data");
        Iterator<Object> it_data = jsonArray.iterator();
        List<String> sqlList = new LinkedList<>();
        List<Object> sqlParameters_yuan = new LinkedList<>();
        while (it_data.hasNext()) {
            List<Object> sqlParameters = new LinkedList<>();
        	StringBuffer sql_parameters = new StringBuffer("id,");
        	StringBuffer sql_values = new StringBuffer("?,");
        	sqlParameters.add(ApplicationUtils.randomUUID());
			JSONObject ob = (JSONObject) it_data.next();
			Iterator it_map = ob.keys();
			while (it_map.hasNext()) {
				String key = (String) it_map.next();  
				sql_parameters.append(key+",");
				sql_values.append("？,");
				sqlParameters.add(ob.get(key));
			}
			String parameters = sql_parameters.substring(0, sql_parameters.lastIndexOf(",")) ;
	        String values = sql_values.substring(0, sql_values.lastIndexOf(",")) ;
	        System.out.println("-------------------------------------------------------------------");
	        String sql = "insert into "+tableName+"("+parameters+") values("+values+")";
	        sqlList.add(sql);
	        sqlParameters_yuan.add(sqlParameters);
	        System.out.println(sql);
	        for(Object o : sqlParameters){
	        	System.out.println(o);
	        }
        }
        System.out.println(sqlList.size()  + "  -----   " + sqlParameters_yuan.size());
    }
    
    public void insertDate(){
    	Connection conn = getConnection();
    	
    }
    
    
    /**
     * 创建一个数据库连接
     * @return
     */
    public Connection getConnection() {
        Connection conn = null;
        //创建数据库连接 
        try {
        	conn = DriverManager.getConnection(url, username, password); 
        } catch (SQLException e) {
        	log.error("#ERROR# :创建数据库连接发生异常，请检查！", e); 
        } 
        return conn; 
    }
    
    /**
     * 关闭 数据库链接 
     * @param conn
     * @param state
     * @param rs
     */
    public void close(Connection conn, Statement state, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (state != null) {
            	state.close();
            }
            if (conn != null) {
            	conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * 在一个数据库连接上执行一批【静态】SQL语句 
     * 
     * @param state        数据库连接
     * @param sqlList 静态SQL语句字符串集合 
     * @throws SQLException 
     */ 
    public void executeBatchSQL(Statement state, List<String> sqlList) throws SQLException { 
		final int batchSize = 500;//单次最大执行条数
	    for (int i = 0 ; i < sqlList.size() ; i++) {
	    	String sql = sqlList.get(i);
	    	state.addBatch(sql); 
	    	if(i % batchSize == 0) {//每加入 500 条 SQL 时，执行插入操作。批量操作。
	    		state.executeBatch();
		    }
	    } 
	    //执行SQL，并获取返回结果 
	    state.executeBatch(); 
    }
}
