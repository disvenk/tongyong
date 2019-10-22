package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.Encrypter;
import com.resto.brand.core.util.JdbcUtils;
import com.resto.brand.web.dao.ShopDetailMapper;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class ShopDetailServiceImpl extends GenericServiceImpl<ShopDetail, String> implements ShopDetailService {

    @Resource
    private ShopDetailMapper shopdetailMapper;

    @Resource
    private BrandService brandService;

    @Override
    public GenericDao<ShopDetail, String> getDao() {
        return shopdetailMapper;
    }

    @Value("${BIGDATA_URL}")
    private String BIGDATA_URL;
    @Value("${BIGDATA_NAME}")
    private String BIGDATA_NAME;
    @Value("${BIGDATA_PASSWORD}")
    private String BIGDATA_PASSWORD;
    @Value("${BIGDATA_DRIVER}")
    private String BIGDATA_DRIVER;
	
	 @Override
    /**
     * 添加门店的详细信息
     */
    public int insertShopDetail(ShopDetail shopDetail) {
	     
         //生成 shopDetail的id并赋值
         String shopDetailId = ApplicationUtils.randomUUID();
         shopDetail.setId(shopDetailId);
        
         //生成当前时间赋值给添加的时间
         Date date = new Date();
         shopDetail.setAddTime(date);
        
         //赋值添加人
         shopDetail.setAddUser("-1");
         //设置更新的人
         shopDetail.setUpdateUser("-1");
        
         //设置状态为1
         shopDetail.setState(1);
        
         //设置店铺的是否开启
         shopDetail.setIsOpen(true);

         int shopCount = shopdetailMapper.insertSelective(shopDetail);

         //新增店铺结束后添加默认的消费体验
         Brand b = brandService.selectById(shopDetail.getBrandId());
         String url = b.getDatabaseConfig().getUrl();
         String driver = b.getDatabaseConfig().getDriverClassName();
         try {
             Class.forName(driver);
             String username = Encrypter.decrypt(b.getDatabaseConfig().getUsername());
             String password = Encrypter.decrypt(b.getDatabaseConfig().getPassword());
             JdbcUtils jdbcUtils = new JdbcUtils(username, password, driver, url);
             jdbcUtils.getConnection();
             String insertSql = "insert into tb_show_photo (show_type,title,shop_detail_id,show_sort) " +
                     "values (2,'食材好','"+shopDetailId+"',1)," +
                     "(2,'口味好','"+shopDetailId+"',2)," +
                     "(2,'上菜快','"+shopDetailId+"',3)," +
                     "(2,'服务好','"+shopDetailId+"',4)," +
                     "(2,'环境好','"+shopDetailId+"',5)," +
                     "(2,'性价比高','"+shopDetailId+"',6)," +
                     "(4,'食材差','"+shopDetailId+"',1)," +
                     "(4,'口味差','"+shopDetailId+"',2)," +
                     "(4,'上菜慢','"+shopDetailId+"',3)," +
                     "(4,'服务差','"+shopDetailId+"',4)," +
                     "(4,'环境差','"+shopDetailId+"',5)," +
                     "(4,'性价比低','"+shopDetailId+"',6)";
             jdbcUtils.updateByPreparedStatement(insertSql, null);
         } catch (ClassNotFoundException e) {
             //e.printStackTrace();
             log.error(e.getMessage());
         } catch (SQLException e) {
             //e.printStackTrace();
             log.error(e.getMessage());
         } finally {
             JdbcUtils.close();
         }
       return  shopCount;
    }

	@Override
	public int deleteBystate(String id) {
		ShopDetail shopDetail = shopdetailMapper.selectByPrimaryKey(id);
		if(shopDetail!=null){
			shopDetail.setState(0);
			
		}
		return shopdetailMapper.updateByPrimaryKeySelective(shopDetail);
		
	}

	@Override
	public  int updateWithDatong(ShopDetail shopDetail,String brandId,String brandName){
        //删除该店铺下在大同数据库存的手机信息
        JdbcUtils jdbcUtils = new JdbcUtils(BIGDATA_NAME,BIGDATA_PASSWORD,BIGDATA_DRIVER,BIGDATA_URL);
        jdbcUtils.getConnection();
        // 删除店铺为**的记录
		  String deleteSql = "delete from tb_sms_user where shop_id = ?";
		  List<Object>params = new ArrayList<>();
		  params.add(shopDetail.getId());
        try {
            boolean flag = jdbcUtils.updateByPreparedStatement(deleteSql, params);
            if(flag){
                log.info("删除大同日结短信数据成功："+shopDetail.getId()+"店铺名称："+shopDetail.getName());
            }
        } catch (SQLException e) {
            log.info("删除大同日结短信数据失败："+shopDetail.getId()+"店铺名称："+shopDetail.getName());
            e.printStackTrace();
        }
        //判断是否是需要日结短信
        //插入数据
        String saveSql = "insert into tb_sms_user (telephone, shop_id,shop_name,brand_id,brand_name,state) values (?,?,?,?,?,?)";
        if(shopDetail.getIsOpenSms()==1){
            for(String s:shopDetail.getNoticeTelephone().split(",")){
                List<Object> saveParams = new ArrayList<>();
                saveParams.add(s);
                saveParams.add(shopDetail.getId());
                saveParams.add(shopDetail.getName());
                saveParams.add(brandId);
                saveParams.add(brandName);
                saveParams.add(1);
                try {
                    boolean flag = jdbcUtils.updateByPreparedStatement(saveSql, saveParams);
                    if(flag){
                        log.info("手机号"+s+"插入店铺id："+shopDetail.getId()+"店铺名称："+shopDetail.getName()+"成功");
                    }
                }catch (SQLException e){
                    log.info("手机号"+s+"插入店铺id："+shopDetail.getId()+"店铺名称："+shopDetail.getName()+"失败");
                }
            }
        }
        jdbcUtils.close();
        return shopdetailMapper.updateByPrimaryKeySelective(shopDetail);
    }


	@Override
	public List<ShopDetail> selectByBrandId(String brandId) {
		return shopdetailMapper.selectByBrandId(brandId);
	}

    @Override
    public ShopDetail selectByPrimaryKey(String id) {
        return shopdetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ShopDetail> selectByShopCity(String brandId) {
        return shopdetailMapper.selectByShopCity(brandId);
    }

    @Override
    public List<ShopDetail> selectByCityOrName(String name,String brandId) {
        return shopdetailMapper.selectByCityOrName(name,brandId);
    }

    @Override
    public List<ShopDetail> selectByCity(String city,String brandId) {
        return shopdetailMapper.selectByCity(city,brandId);
    }

    @Override
    public ShopDetail selectByRestaurantId(Integer restaurantId) {
        return shopdetailMapper.selectByRestaurantId(restaurantId);
    }

    @Override
    public ShopDetail selectByOOrderShopId(Long shopId) {
        return shopdetailMapper.selectByOOrderShopId(shopId);
    }

    @Override
    public List<ShopDetail> selectOrderByIndex(String brandId) {
        return shopdetailMapper.selectOrderByIndex(brandId);
    }

    @Override
    public void updateGeekposQueueLoginState(String id,String state) {
        ShopDetail shopDetail = shopdetailMapper.selectByPrimaryKey(id);
        if(shopDetail!=null){
            if(state.equals("signin")){
                shopDetail.setGeekposQueueIslogin(1);
                shopDetail.setGeekposQueueLastLoginTime(new Date());
            }else if(state.equals("signout")){
                shopDetail.setGeekposQueueIslogin(0);
            }
        }
        shopdetailMapper.updateByPrimaryKeySelective(shopDetail);
    }

    @Override
    public ShopDetail selectQueueInfo(String shopId) {
        return shopdetailMapper.selectQueueInfo(shopId);
    }

    @Override
    public ShopDetail selectByThirdAppId(String thirdAppid) {
       return shopdetailMapper.selectByThirdAppId(thirdAppid);
    }

    @Override
    public int updatePosWaitredEnvelope(String id, Integer state) {
        return shopdetailMapper.updatePosWaitredEnvelope(id, state);
    }

    @Override
    public ShopDetail selectByPosKey(String key) {
        return shopdetailMapper.selectByPosKey(key);
    }

    @Override
    public ShopDetail posSelectById(String shopId) {
        return shopdetailMapper.posSelectById(shopId);
    }

    @Override
    public List<ShopDetail> selectByShopAndByName(@Param("brandId") String brandId, @Param("name") String name) {
        return shopdetailMapper.selectByShopAndByName(brandId,name);
    }

    @Override
    public ShopDetail selectShopByStatus(String brandId, String ShopId) {
        return shopdetailMapper.selectShopStatus(brandId,ShopId);
    }
}
