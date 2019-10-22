//package com.resto.brand.web.task;
//
//import com.resto.brand.core.util.ApplicationUtils;
//import com.resto.brand.core.util.Encrypter;
//import com.resto.brand.core.util.JdbcUtils;
//import com.resto.brand.web.service.impl.WeChatUtils;
//import com.resto.brand.web.model.Brand;
//import com.resto.brand.web.model.DatabaseConfig;
//import com.resto.brand.web.service.BrandService;
//import com.resto.brand.web.service.DatabaseConfigService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by carl on 2016/12/26.
// */
//@Component("changOrderTask")
//public class ChangOrderTask {
//
//    @Autowired
//    private DatabaseConfigService databaseConfigService;
//    @Autowired
//    private BrandService brandService;
//
//    //@Scheduled(cron = "0/10 * *  * * ? ")   //每5秒执行一次
//    @Scheduled(cron = "0 0 7 * * ?")   //每天7点
//    public void job1() throws ClassNotFoundException {
//        List<Brand> brands = brandService.selectList();
//
//        String url = null;
//        String driver = null;
//        String username = null;
//        String password = null;
//        Connection con = null;
//        Statement st = null;
//        ResultSet rs = null;
//        int row = 0;
//        for (Brand brand : brands) {
//            try {
//                //查询 选中的数据库信息
//                Brand b = brandService.selectById(brand.getId());
//                url = b.getDatabaseConfig().getUrl();
//                driver = b.getDatabaseConfig().getDriverClassName();
//                Class.forName(driver);
//                username = Encrypter.decrypt(b.getDatabaseConfig().getUsername());
//                password = Encrypter.decrypt(b.getDatabaseConfig().getPassword());
//                JdbcUtils jdbcUtils = new JdbcUtils(username, password, driver, url);
//                jdbcUtils.getConnection();
//
//                //查询所有没有完成的充值光的记录
//                String sql = "SELECT * FROM tb_charge_order t where t.order_state = 1 and t.number_day_now > 0";
//                List<Map<String, Object>> list = jdbcUtils.findModeResult(sql,null);
//
//                for(int i = 0; i < list.size(); i++){
//                    Map<String, Object> map = (Map) list.get(i);
//                    //当前记录信息
////                        String selectSql = "select * from tb_charge_order where id='" + map.get("id") + "'";
////                        Map<String, Object> m = jdbcUtils.findSimpleResultWithoutClose(selectSql,null);
//                    //修改记录信息
//                    Integer nowNumber = Integer.parseInt(map.get("number_day_now").toString());
//                    if(nowNumber == 1){
//                        String updateSql = "UPDATE tb_charge_order t set t.number_day_now=" + (Integer.parseInt(map.get("number_day_now").toString()) - 1) + " , " +
//                                "t.reward_balance ="+(getBigDecimal(map.get("reward_balance")).add(getBigDecimal(map.get("end_amount"))))+" , " +
//                                "t.total_balance="+(getBigDecimal(map.get("total_balance")).add(getBigDecimal(map.get("end_amount"))))+" where t.id='" + map.get("id") + "'";
//                        jdbcUtils.updateByPreparedStatement(updateSql, null);
//                        //查询用户的account_id
//                        String selectSqlTwo = "select t.account_id,t1.remain from tb_customer t LEFT JOIN tb_account t1 on t1.id = t.account_id where t.id='" + map.get("customer_id") + "'";
//                        Map<String, Object> a = jdbcUtils.findSimpleResultWithoutClose(selectSqlTwo,null);
//                        if(a != null){
//                            //记录tb_account_log
//                            String insertSql = "insert into tb_account_log (id, money, create_time, payment_type, remain, remark, account_id, source) " +
//                                    "values ('" + ApplicationUtils.randomUUID() + "', " + getBigDecimal(map.get("end_amount")) + ", now(), " +
//                                    "1, "+(getBigDecimal(a.get("remain")).add(getBigDecimal(map.get("end_amount"))))+", '充值赠送', '" + a.get("account_id") + "', 3)";
//                            jdbcUtils.updateByPreparedStatement(insertSql, null);
//                            //修改tb_account的余额记录
//                            String updateSqlTwo = "UPDATE tb_account t set t.remain =" + (getBigDecimal(a.get("remain")).add(getBigDecimal(map.get("end_amount")))) +
//                                    " where t.id ='" + a.get("account_id") + "'";
//                            jdbcUtils.updateByPreparedStatement(updateSqlTwo, null);
//                        }
//                    }else{
//                        String updateSql = "UPDATE tb_charge_order t set t.number_day_now=" + (Integer.parseInt(map.get("number_day_now").toString()) - 1) + " , " +
//                                "t.reward_balance ="+(getBigDecimal(map.get("reward_balance")).add(getBigDecimal(map.get("arrival_amount"))))+" , " +
//                                "t.total_balance="+(getBigDecimal(map.get("total_balance")).add(getBigDecimal(map.get("arrival_amount"))))+" where t.id='" + map.get("id") + "'";
//                        jdbcUtils.updateByPreparedStatement(updateSql, null);
//                        //查询用户的account_id
//                        String selectSqlTwo = "select t.account_id,t1.remain from tb_customer t LEFT JOIN tb_account t1 on t1.id = t.account_id where t.id='" + map.get("customer_id") + "'";
//                        Map<String, Object> a = jdbcUtils.findSimpleResultWithoutClose(selectSqlTwo,null);
//                        if(a != null){
//                            //记录tb_account_log
//                            String insertSql = "insert into tb_account_log (id, money, create_time, payment_type, remain, remark, account_id, source) " +
//                                    "values ('" + ApplicationUtils.randomUUID() + "', " + getBigDecimal(map.get("arrival_amount")) + ", now(), " +
//                                    "1, "+(getBigDecimal(a.get("remain")).add(getBigDecimal(map.get("arrival_amount"))))+", '充值赠送', '" + a.get("account_id") + "', 3)";
//                            jdbcUtils.updateByPreparedStatement(insertSql, null);
//                            //修改tb_account的余额记录
//                            String updateSqlTwo = "UPDATE tb_account t set t.remain =" + (getBigDecimal(a.get("remain")).add(getBigDecimal(map.get("arrival_amount")))) +
//                                    " where t.id ='" + a.get("account_id") + "'";
//                            jdbcUtils.updateByPreparedStatement(updateSqlTwo, null);
//                        }
//                    }
//                    //用户信息表查询
//                    String selectSqlThree = "select * from tb_customer t where t.id='"+map.get("customer_id")+"'";
//                    Map<String, Object> customer = jdbcUtils.findSimpleResultWithoutClose(selectSqlThree,null);
//                    //推送
//                    StringBuffer msg = new StringBuffer();
//                    msg.append("今日充值赠送红包已到账，快去看看吧~");
//                    String jumpurl = "http://" + brand.getBrandSign() + ".restoplus.cn/wechat/index?dialog=myYue&subpage=my";
//                    msg.append("<a href='" + jumpurl+ "'>查看账户</a>");
//                    WeChatUtils.sendCustomerMsg(msg.toString(), customer.get("wechat_id").toString(), b.getWechatConfig().getAppid(), b.getWechatConfig().getAppsecret());
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } finally {
//                JdbcUtils.close();
//            }
//        }
//    }
//
//    public static BigDecimal getBigDecimal(Object value) {
//        BigDecimal ret = null;
//        if( value != null ) {
//            if( value instanceof BigDecimal ) {
//                ret = (BigDecimal) value;
//            } else if( value instanceof String ) {
//                ret = new BigDecimal( (String) value );
//            } else if( value instanceof BigInteger ) {
//                ret = new BigDecimal( (BigInteger) value );
//            } else if( value instanceof Number ) {
//                ret = new BigDecimal( ((Number)value).doubleValue() );
//            } else {
//                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
//            }
//        }
//        return ret;
//    }
//}
