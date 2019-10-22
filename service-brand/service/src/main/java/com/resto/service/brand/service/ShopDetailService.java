package com.resto.service.brand.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.resto.conf.util.ApplicationUtils;
import com.resto.conf.util.Encrypter;
import com.resto.conf.util.JdbcUtils;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.Brand;
import com.resto.service.brand.entity.ShopDetail;
import com.resto.service.brand.mapper.ShopDetailMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Service
public class ShopDetailService extends BaseService<ShopDetail, String> {

    @Autowired
    private ShopDetailMapper shopdetailMapper;

    @Autowired
    private BrandService brandService;

    public BaseDao<ShopDetail, String> getDao() {
        return shopdetailMapper;
    }

//    @Value("${BIGDATA_URL}")
    private String BIGDATA_URL;
//    @Value("${BIGDATA_NAME}")
    private String BIGDATA_NAME;
//    @Value("${BIGDATA_PASSWORD}")
    private String BIGDATA_PASSWORD;
//    @Value("${BIGDATA_DRIVER}")
    private String BIGDATA_DRIVER;

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
             logger.error(e.getMessage());
         } catch (SQLException e) {
             //e.printStackTrace();
             logger.error(e.getMessage());
         } finally {
             JdbcUtils.close();
         }
       return  shopCount;
    }

	public int deleteBystate(String id) {
		ShopDetail shopDetail = shopdetailMapper.selectByPrimaryKey(id);
		if(shopDetail!=null){
			shopDetail.setState(0);
			
		}
		return shopdetailMapper.updateByPrimaryKeySelective(shopDetail);
		
	}

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
                logger.info("删除大同日结短信数据成功："+shopDetail.getId()+"店铺名称："+shopDetail.getName());
            }
        } catch (SQLException e) {
            logger.info("删除大同日结短信数据失败："+shopDetail.getId()+"店铺名称："+shopDetail.getName());
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
                        logger.info("手机号"+s+"插入店铺id："+shopDetail.getId()+"店铺名称："+shopDetail.getName()+"成功");
                    }
                }catch (SQLException e){
                    logger.info("手机号"+s+"插入店铺id："+shopDetail.getId()+"店铺名称："+shopDetail.getName()+"失败");
                }
            }
        }
        jdbcUtils.close();
        return shopdetailMapper.updateByPrimaryKeySelective(shopDetail);
    }


	public List<ShopDetail> selectByBrandId(String brandId) {
		return shopdetailMapper.selectByBrandId(brandId);
	}

    public ShopDetail selectByPrimaryKey(String id) {
        return shopdetailMapper.selectByPrimaryKey(id);
    }

    public List<ShopDetail> selectByShopCity(String brandId) {
        return shopdetailMapper.selectByShopCity(brandId);
    }


    public List<ShopDetail> selectByCityOrName(String name,String brandId) {
	    List<ShopDetail> shopDetails = shopdetailMapper.selectByCityOrName(name,brandId);
	    for (ShopDetail shopDetail : shopDetails){
	        if (StringUtils.isNotBlank(shopDetail.getShopTag())) {
                String[] shopTags = shopDetail.getShopTag().split(",");
                String json = JSON.toJSONString(shopTags);
                List<String> jsonList = JSON.parseObject(json, new TypeReference<List<String>>(){});
                shopDetail.setShopTags(jsonList);
            }
        }
        return shopDetails;
    }

    public List<ShopDetail> selectByCity(String city,String brandId) {
        return shopdetailMapper.selectByCity(city,brandId);
    }

    public ShopDetail selectByRestaurantId(Integer restaurantId) {
        return shopdetailMapper.selectByRestaurantId(restaurantId);
    }

    public ShopDetail selectByOOrderShopId(Long shopId) {
        return shopdetailMapper.selectByOOrderShopId(shopId);
    }

    public List<ShopDetail> selectOrderByIndex(String brandId) {
        return shopdetailMapper.selectOrderByIndex(brandId);
    }


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

    public ShopDetail selectQueueInfo(String shopId) {
        return shopdetailMapper.selectQueueInfo(shopId);
    }

    public ShopDetail selectByThirdAppId(String thirdAppid) {
       return shopdetailMapper.selectByThirdAppId(thirdAppid);
    }

    public int updatePosWaitredEnvelope(String id, Integer state) {
        return shopdetailMapper.updatePosWaitredEnvelope(id, state);
    }

    public List<ShopDetail> getShopDetailsListParams(List<String> shopDetailIds){
        List<ShopDetail> list = shopdetailMapper.getShopDetailsListParams(shopDetailIds);
        return list;
    }
}
