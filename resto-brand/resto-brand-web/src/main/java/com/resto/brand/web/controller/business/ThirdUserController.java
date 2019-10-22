package com.resto.brand.web.controller.business;

import com.resto.brand.core.util.Encrypter;
import com.resto.brand.core.util.JdbcUtils;
import com.resto.brand.web.dto.User;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.util.ReadExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * Created by carl on 2017/8/25.
 */
@RequestMapping("thirdUser")
@RestController
public class ThirdUserController {

    public Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private BrandService brandService;

    @RequestMapping("/upload")
    @ResponseBody
    public String  upload(@RequestParam(value="file",required = false)MultipartFile file, String brandSettingId,
                          HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = readExcelFile(file, brandSettingId);
        return result;
    }

    public String readExcelFile(MultipartFile file, String brandSettingId) {
        String result ="";
        //创建处理EXCEL的类
        ReadExcel readExcel=new ReadExcel();
        //解析excel，获取上传的事件单
        List<User> useList = readExcel.getExcelInfo(file);
        //至此已经将excel中的数据转换到list里面了,接下来就可以操作list,可以进行保存到数据库,或者其他操作,
        //和你具体业务有关,这里不做具体的示范
        if(useList != null && !useList.isEmpty()){
            //插入表
            Brand b = brandService.selectBrandBySetting(brandSettingId);
            b = brandService.selectById(b.getId());
            String url = b.getDatabaseConfig().getUrl();
            String driver = b.getDatabaseConfig().getDriverClassName();
            try {
                Class.forName(driver);
                String username = Encrypter.decrypt(b.getDatabaseConfig().getUsername());
                String password = Encrypter.decrypt(b.getDatabaseConfig().getPassword());
                JdbcUtils jdbcUtils = new JdbcUtils(username, password, driver, url);
                jdbcUtils.getConnection();
                for(int i = 0; i < useList.size(); i++){
                    try {
                        String sql = "SELECT * FROM tb_third_customer t where t.telephone = '"+useList.get(i).getTelephone()+"'";
                        List<Map<String, Object>> list = jdbcUtils.findModeResult(sql,null);
                        if(list.size() > 0){
                            Map<String, Object> map = (Map) list.get(0);
                            String updateSqlTwo = "UPDATE tb_third_customer t set t.money =" + (getBigDecimal(map.get("money")).add(useList.get(i).getMoney())) +
                                    " where t.telephone ='" + useList.get(i).getTelephone() + "'";
                            jdbcUtils.updateByPreparedStatement(updateSqlTwo, null);
                        }else{
                            String insertSql = "insert into tb_third_customer (telephone,money,remark) " +
                                    "values ('"+useList.get(i).getTelephone()+"','"+useList.get(i).getMoney()+"','"+useList.get(i).getRemark()+"')";
                            jdbcUtils.updateByPreparedStatement(insertSql, null);
                        }
                    } catch (Exception e){
                        log.error(e.getMessage());
                    }
                }
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                log.error(e.getMessage());
            } finally {
                JdbcUtils.close();
            }
            result = "上传成功";
        }else{
            result = "上传失败";
        }
        return result;
    }

    public static BigDecimal getBigDecimal(Object value) {
        BigDecimal ret = null;
        if( value != null ) {
            if( value instanceof BigDecimal ) {
                ret = (BigDecimal) value;
            } else if( value instanceof String ) {
                ret = new BigDecimal( (String) value );
            } else if( value instanceof BigInteger) {
                ret = new BigDecimal( (BigInteger) value );
            } else if( value instanceof Number ) {
                ret = new BigDecimal( ((Number)value).doubleValue() );
            } else {
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
            }
        }
        return ret;
    }
}
