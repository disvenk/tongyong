package com.resto.shop.web.util;

import com.resto.api.appraise.entity.Appraise;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.NumberUtil;
import com.resto.brand.web.dto.ArticleTopDto;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.shop.web.model.DayDataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yz on 2017-06-12.
 */
public class JdbcSmsUtils {
    // 数据库用户名
    private static final String USERNAME = "root";
    // 数据库密码
    private static final String PASSWORD = "123456";
    // 驱动信息
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    // 数据库地址
    private static final String URL = "jdbc:mysql://101.200.190.249:3306/test?useUnicode=true&characterEncoding=utf8";

    private static Connection connection;
    private static PreparedStatement pstmt;

    static   Logger log = LoggerFactory.getLogger(JdbcSmsUtils.class);

    private static Boolean todayAppraiseFlag = true;

    private static Boolean goodTopFlag = true;

    private static Boolean badTopFlag = true;

    /**
     * 获得数据库的连接
     *
     * @return
     */
    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            log.info("---数据库连接成功---");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    /**
     * 释放数据库连接
     */
    public static void close() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * 增加、删除、改
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static boolean updateByPreparedStatement(String sql, List<Object> params) throws SQLException {
        boolean flag = false;
        int result = -1;
        pstmt = connection.prepareStatement(sql);
        int index = 1;
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(index++, params.get(i));
            }
        }
        result = pstmt.executeUpdate();
        flag = result > 0 ? true : false;
        return flag;
    }

    /**
     *
     * @param dayDataMessage
     * @return
     * 插入日结短信数据
     * 这里面的数据是一次性插入所以可以不管flag
     */
    public  static  void saveDayDataMessage(DayDataMessage dayDataMessage,String shopId) {
        //初始化连接
        log.info("开始插入日结短信数据---");
        getConnection();
        //删除今日数据
        String deleteSql = "delete from tb_day_data_message where shop_id = ? and date = ?";
        List<Object> deleteParams = new ArrayList<>();
        deleteParams.add(shopId);
        deleteParams.add(DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
        try{
            updateByPreparedStatement(deleteSql,deleteParams);
        }catch (SQLException e){
            e.printStackTrace();
        }

        String sql = "INSERT INTO tb_day_data_message(id,shop_id,type,shop_name,week_day,date,times,wether,temperature,order_number,order_sum,customer_order_number,customer_order_sum,customer_order_ratio,back_customer_order_ratio,new_customer_order_ratio,new_cuostomer_order_num,new_customer_order_sum,new_normal_customer_order_num,new_normal_customer_order_sum,new_share_customer_order_num,new_share_customer_order_sum,back_customer_order_num,back_customer_order_sum,back_two_customer_order_num,back_two_customer_order_sum,back_two_more_customer_order_num,back_two_more_customer_order_sum,discount_total,red_pack,coupon,charge_reward,discount_ratio,takeaway_total,bussiness_total,month_total)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        List<Object> params = new ArrayList<>();
        params.add(ApplicationUtils.randomUUID());//id
        params.add(dayDataMessage.getShopId());//shop_id
        params.add(dayDataMessage.getType()); //type
        params.add(dayDataMessage.getShopName());//shop_name
        params.add(dayDataMessage.getWeekDay());//week_day
        params.add(dayDataMessage.getDate());//date
        params.add(dayDataMessage.getTimes());//times
        params.add(dayDataMessage.getWether());//wether
        params.add(dayDataMessage.getTemperature());//temperature
        params.add(dayDataMessage.getOrderNumber());//10 order_number
        params.add(dayDataMessage.getOrderSum());//order_sum
        params.add(dayDataMessage.getCustomerOrderNumber());//customer_order_number
        params.add(dayDataMessage.getCustomerOrderSum());//customer_order_sum
        params.add(dayDataMessage.getCustomerOrderRatio());//customer_order_ratio
        params.add(dayDataMessage.getBackCustomerOrderRatio());//back_customer_order_ratio
        params.add(dayDataMessage.getNewCustomerOrderRatio());//new_customer_order_ratio
        params.add(dayDataMessage.getNewShareCustomerOrderNum());//new_cuostomer_order_num
        params.add(dayDataMessage.getNewShareCustomerOrderSum());//new_customer_order_sum
        params.add(dayDataMessage.getNewNormalCustomerOrderNum());//new_normal_customer_order_num
        params.add(dayDataMessage.getNewNormalCustomerOrderSum());//20 new_normal_customer_order_sum
        params.add(dayDataMessage.getNewShareCustomerOrderNum());//new_share_customer_order_num
        params.add(dayDataMessage.getNewShareCustomerOrderSum());//new_share_customer_order_sum
        params.add(dayDataMessage.getBackCustomerOrderNum());//back_customer_order_num
        params.add(dayDataMessage.getBackCustomerOrderSum());//back_customer_order_sum
        params.add(dayDataMessage.getBackTwoCustomerOrderNum());//back_two_customer_order_num
        params.add(dayDataMessage.getBackTwoCustomerOrderSum());//back_two_customer_order_sum
        params.add(dayDataMessage.getBackTwoMoreCustomerOrderNum());//back_two_more_customer_order_num
        params.add(dayDataMessage.getBackTwoMoreCustomerOrderSum());//back_two_more_customer_order_sum
        params.add(dayDataMessage.getDiscountTotal());//discount_total
        params.add(dayDataMessage.getRedPack());//red_pack
        params.add(dayDataMessage.getCoupon());//coupon
        params.add(dayDataMessage.getChargeReward());//charge_reward
        params.add(dayDataMessage.getDiscountRatio());//discount_ratio
        params.add(dayDataMessage.getTakeawayTotal());//takeaway_total
        params.add(dayDataMessage.getBussinessTotal());//bussiness_total
        params.add(dayDataMessage.getMonthTotal());//month_total
        try {
            updateByPreparedStatement(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }


    /**
     * 存今日评论数据
     * @param a
     * 这数据是循环插入 所以不能每次都删除原有数据 第一次删除
     *
     */
    public static void saveTodayAppraise(Appraise a, String brandId, String shopId) {
        //初始化连接
        log.info("开始存当日评论数据---");
        getConnection();
        if(todayAppraiseFlag){
            //首先删除今日店铺的评论数据(防止多次结店)
            String deleteSql = "DELETE FROM tb_sms_appraise WHERE shop_detail_id = ? and create_time > ? and create_time<?";
            List<Object> dparams = new ArrayList<>();
            dparams.add(shopId);
            dparams.add(DateUtil.getDateBegin(new Date()));
            dparams.add(DateUtil.getDateEnd(new Date()));
            try {
                updateByPreparedStatement(deleteSql,dparams);
                //第一次删除后
                todayAppraiseFlag = false;
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        String sql = "INSERT into tb_sms_appraise (id,picture_url,level,create_time,content,status,type,feedback,shop_detail_id,brand_id,head_photo,nick_name,sex) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        List<Object> params = new ArrayList<>();
        params.add(ApplicationUtils.randomUUID());
        params.add(a.getPictureUrl());
        params.add(a.getLevel());
        params.add(a.getCreateTime());
        params.add(EmojiFilter.filterEmoji(a.getContent()));
        params.add(a.getStatus());
        params.add(a.getType());
        params.add(a.getFeedback());
        params.add(a.getShopDetailId());
        params.add(brandId);
        if(a.getHeadPhoto()==null){
            a.setHeadPhoto("");
        }
        params.add(a.getHeadPhoto());
        if(a.getNickName()==null){
            a.setNickName("");
        }
        params.add(EmojiFilter.filterEmoji(a.getNickName()));
        params.add(a.getSex());
        try{
          updateByPreparedStatement(sql,params);

        }catch (SQLException e){
            e.printStackTrace();
        }
        log.info("存评论数据成功");
        close();
    }

    /**
     * 这个数据也是一次性 所以每次删除就可以不用考虑 flag
     * @param todaySatisfaction
     * @param xunSatisfaction
     * @param monthSatisfaction
     * @param brandId
     * @param shopId
     */
    public static void saveStations(String todaySatisfaction, String xunSatisfaction, String monthSatisfaction,String brandId,String shopId) {

        //初始化连接
        getConnection();

        //先删除今日的数据
        String deleteSql = "DELETE from tb_statisfaction where shop_id = ? and date = ?";
        List<Object> dParams = new ArrayList<>();
        dParams.add(shopId);
        dParams.add(DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
        try {
            updateByPreparedStatement(deleteSql,dParams);
            log.info("删除今日满意度成功------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //新增数据
        String sql = "INSERT into tb_statisfaction (id ,shop_id,brand_id ,date ,today_stations ,xun_stations ,month_stations) values (?,?,?,?,?,?,?)";
        List<Object> params = new ArrayList<>();
        params.add(ApplicationUtils.randomUUID());
        params.add(shopId);
        params.add(brandId);
        params.add(new Date());
        params.add(todaySatisfaction+"%");
        params.add(xunSatisfaction+"%");
        params.add(monthSatisfaction+"%");

        try {
            updateByPreparedStatement(sql,params);
            log.info("存满意度数据。。。");
        }catch (SQLException e){
            e.printStackTrace();
        }

        close();

    }

    /**
     *存goodTop 10
     * @param articleTopDto
     * @param b
     * @param s
     * @param type
     * @param sum
     * @param sort
     * 该数据是循环插入所以需要判断flag
     *
     */
    public static void saveGoodTop(ArticleTopDto articleTopDto, Brand b, ShopDetail s ,int type,int sum,int sort) {
        //初始化连接
        getConnection();

        if(goodTopFlag){
            //删除数据(防止多次结店数据多)
            String dsql = "delete from tb_good_top where shop_id = ? and date = ? and type = ? and sort = ?";
            List<Object> dparams = new ArrayList<>();
            dparams.add(s.getId());
            dparams.add(DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
            dparams.add(type);
            dparams.add(sort);
            try {
                updateByPreparedStatement(dsql,dparams);
                goodTopFlag = false;
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        //新增数据
        String sql = "insert into tb_good_top (id ,name ,precent, sort,shop_id,brand_id,shop_name,brand_name,date,type)values(?,?,?,?,?,?,?,?,?,?)";
        List<Object> params = new ArrayList<>();
        params.add(ApplicationUtils.randomUUID());
        params.add(articleTopDto.getName());
        params.add(NumberUtil.getFormat(articleTopDto.getNum(),sum));
        params.add(sort);
        params.add(s.getId());
        params.add(b.getId());
        params.add(s.getName());
        params.add(b.getBrandName());
        params.add(new Date());
        params.add(type);
        try {
            updateByPreparedStatement(sql,params);
        }catch (SQLException e){

            e.printStackTrace();
        }
        close();

    }

    /**
     * 存badTop10
     * @param articleTopDto
     * @param b
     * @param s
     * @param type
     * @param sum
     * @param sort
     */
    public static void saveBadTop(ArticleTopDto articleTopDto, Brand b, ShopDetail s, int type, int sum, int sort) {

        //初始化连接
        getConnection();

        if(badTopFlag){
            //删除数据(防止多次结店数据多)
            String dsql = "delete from tb_bad_top where shop_id = ? and date = ? and type = ? and sort = ?";
            List<Object> dparams = new ArrayList<>();
            dparams.add(s.getId());
            dparams.add(DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
            dparams.add(type);
            dparams.add(sort);

            try {
                updateByPreparedStatement(dsql,dparams);
                badTopFlag = false;
            }catch (SQLException e){
                e.printStackTrace();
            }

        }

        //新增数据
        String sql = "insert into tb_bad_top (id ,name ,precent, sort,shop_id,brand_id,shop_name,brand_name,date,type)values(?,?,?,?,?,?,?,?,?,?)";
        List<Object> params = new ArrayList<>();
        params.add(ApplicationUtils.randomUUID());
        params.add(articleTopDto.getName());
        params.add(NumberUtil.getFormat(articleTopDto.getNum(),sum));
        params.add(sort);
        params.add(s.getId());
        params.add(b.getId());
        params.add(s.getName());
        params.add(b.getBrandName());
        params.add(new Date());
        params.add(type);
        try {
            updateByPreparedStatement(sql,params);
        }catch (SQLException e){

            e.printStackTrace();
        }
        close();


    }








    public static void main(String[] args) throws SQLException {
        //初始化连接
        getConnection();

        //先删除今日的数据
        String deleteSql = "DELETE from tb_statisfaction where shop_id = ? and date = ?";
        List<Object> dParams = new ArrayList<>();
        dParams.add("123456");
        dParams.add(DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
        try {
            updateByPreparedStatement(deleteSql,dParams);
            log.info("删除今日满意度成功------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();

    }



}
