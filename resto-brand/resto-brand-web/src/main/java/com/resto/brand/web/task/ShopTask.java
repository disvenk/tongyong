//package com.resto.brand.web.task;
//
//import com.resto.brand.core.enums.RoleSign;
//import com.resto.brand.core.util.DateUtil;
//import com.resto.brand.web.model.DatabaseConfig;
//import com.resto.brand.web.service.DatabaseConfigService;
//import org.apache.commons.lang3.time.DateFormatUtils;
//import org.apache.shiro.authz.annotation.RequiresRoles;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.sql.*;
//import java.util.*;
//import java.util.Date;
//
///**
// * Created by KONATA on 2016/7/14.
// */
//@RequiresRoles({RoleSign.SUPER_ADMIN})
//@Component("shopTask")
//public class ShopTask {
//
//    @Autowired
//    private DatabaseConfigService databaseConfigService;
//
//
////    @Autowired
////    private FreedayService freedayService;
//
//
////    @Scheduled(cron = "0/5 * *  * * ? ")   //每5秒执行一次
//    @Scheduled(cron = "59 59 23 * * ?")   //每5秒执行一次
//    public void job1() throws ClassNotFoundException {
//        List<DatabaseConfig> databaseConfigs = databaseConfigService.selectList();
//        String url = null;
//        String driver = null;
//        String username = null;
//        String password = null;
//        Connection con = null;
//        Statement st = null;
//        ResultSet rs = null;
//
//        int row = 0;
//        for (DatabaseConfig databaseConfig : databaseConfigs) {
//            try {
//                //查询 选中的数据库信息
//                DatabaseConfig config = databaseConfigService.selectByPrimaryKey(databaseConfig.getId());
//
//                url = config.getUrl();
//                driver = config.getDriverClassName();
//                Class.forName(driver);
//                username = config.getUsername();
//                password = config.getPassword();
//                con = DriverManager.getConnection(url, username, password);
//                st = con.createStatement();// 创建sql执行对象
//
//
//                String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
//                String getTime = "select * from tb_free_day " +
//                        "  where free_day=" + "'"+ time+"'";
//                rs = st.executeQuery(getTime);
//                Boolean isWorkDay = true;
//                if (rs.next()) {
//                    isWorkDay = false;
//                }
//
//                String day = isWorkDay == true ? "stock_working_day" : "stock_weekend";
//
//                String currentSql = "update tb_article set is_empty = 0 ,empty_remark = NULL , current_working_stock " +
//                        "= " + day;
//
//
//
//
//                if (currentSql.startsWith("select")) {
//                    rs = st.executeQuery(currentSql);
//                } else {
//                    row += st.executeUpdate(currentSql);
//                }
//
//                //初始化有规格餐品库存
//                String sql = "update tb_article_price set empty_remark = NULL , current_working_stock = " + day;
//                st.executeUpdate(sql);
//
//                //初始化套餐库存
//                String initSuit = "update tb_article t , " +
//                        "( " +
//                        "select min(count) as count,article_name,id from (select max(t4.current_working_stock) as count ,t2.name,t.name as article_name,t.id from tb_article t " +
//                        "INNER JOIN tb_meal_attr t2 on t2.article_id = t.id " +
//                        "INNER JOIN tb_meal_item t3 on t3.meal_attr_id = t2.id " +
//                        "INNER JOIN tb_article t4 on t4.id = t3.article_id " +
//                        "where t.article_type = 2 " +
//                        "" +
//                        "GROUP BY t2.name " +
//                        ") as r " +
//                        "GROUP BY article_name " +
//                        ") as b " +
//                        "set t.current_working_stock = b.count " +
//                        "where t.id = b.id ";
//                st.executeUpdate(initSuit);
//                //初始化有规格餐品主品库存(等于其子品库存之和)
//                String initSize = " update tb_article t ,( " +
//                        "    select article_id, sum(current_working_stock) as current_working_stock_count " +
//                        "    from tb_article_price " +
//                        "    GROUP BY article_id " +
//                        "    ) as t2 " +
//                        "    set t.current_working_stock = t2.current_working_stock_count " +
//                        "    where t.id = t2.article_id ";
//                st.executeUpdate(initSize);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } finally {
//                close(con, st, rs);
//            }
//        }
//    }
//
//
//    // 关闭相关的对象
//    public void close(Connection con, Statement st, ResultSet rs) {
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
//}
