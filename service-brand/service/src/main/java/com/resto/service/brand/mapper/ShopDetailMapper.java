package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.ShopDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDetailMapper  extends BaseDao<ShopDetail,String> {
    int deleteByPrimaryKey(String id);

    int insert(ShopDetail record);

    int insertSelective(ShopDetail record);

    ShopDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ShopDetail record);

    int updateByPrimaryKey(ShopDetail record);
    
    /**
     * 根据品牌ID 查询店铺的id和名称
     * @param brandId
     * @return
     */
    List<ShopDetail> selectIdAndName(String brandId);

    /**
     * 根据品牌ID查询所有的店铺
     * @param brandId
     * @return
     */
	List<ShopDetail> selectByBrandId(String brandId);

    /**
     * 查询当前品牌下所有店铺所在的所有地址
     * @param brandId
     * @return
     */
    List<ShopDetail> selectByShopCity(String brandId);

    /**
     * 条件查询城市、店铺名满足需求的所有店铺
     * @param name
     * @return
     */
    List<ShopDetail> selectByCityOrName(@Param("name") String name, @Param("brandId") String brandId);

    List<ShopDetail> selectByCity(@Param("city") String city, @Param("brandId") String brandId);

    ShopDetail selectByRestaurantId(Integer restaurantId);

    /**
     * shopid为饿了么的shopid
     * @param shopId
     * @return
     */
    ShopDetail selectByOOrderShopId(Long shopId);

    List<ShopDetail> selectOrderByIndex(String brandId);
    
    /**
     * 查询店铺 排位的基本信息
     * @author lmx
     * @version 创建时间：2016年12月13日 下午4:38:17
     * @param shopId
     * @return
     */
    ShopDetail selectQueueInfo(String shopId);

    ShopDetail selectByThirdAppId(String thirdAppid);

    int updatePosWaitredEnvelope(@Param("id") String id, @Param("state") Integer state);

    List<ShopDetail> getShopDetailsListParams(@Param("shopDetailIds")List<String> shopDetailIds);
}
