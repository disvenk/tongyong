package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.*;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("daTong")
public class DaTongController extends GenericController {

    @Value("${BIGDATA_URL}")
    private String BIGDATA_URL;
    @Value("${BIGDATA_NAME}")
    private String BIGDATA_NAME;
    @Value("${BIGDATA_PASSWORD}")
    private String BIGDATA_PASSWORD;
    @Value("${BIGDATA_DRIVER}")
    private String BIGDATA_DRIVER;

    @Resource
    private  ShopDetailService shopDetailService;


    @RequestMapping("saveSmsAccount")
    @ResponseBody
    public  Result saveSmsAccount() throws SQLException {
        JdbcUtils jdbcUtils = new JdbcUtils(BIGDATA_NAME,BIGDATA_PASSWORD,BIGDATA_DRIVER,BIGDATA_URL);
        jdbcUtils.getConnection();
        String sql ="insert into tb_sms_user (telephone, shop_id,shop_name,brand_id,brand_name,state) values (?, ?,?,?,?,?)";
        List<ShopDetail> shopDetailList = shopDetailService.selectList();

        for(ShopDetail s:shopDetailList){
            if(s.getIsOpenSms()==1){
                for(String s1:s.getNoticeTelephone().split(",")){
                    List<Object> params = new ArrayList<>();
                    params.add(s1);
                    params.add(s.getId());
                    params.add(s.getName());
                    params.add(s.getBrandId());
                    params.add(s.getBrandName());
                    params.add(true);
                    jdbcUtils.updateByPreparedStatement(sql,params);
                }
              }
            }
        jdbcUtils.close();
        return  Result.getSuccess();

    }



}
