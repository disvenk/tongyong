package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.ShopDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDetailService extends GenericService<ShopDetail, String> {
    int insertShopDetail(ShopDetail shopDetail);

	int deleteBystate(String id);

    ShopDetail selectShopByStatus(String brandId,String ShopId);

	List<ShopDetail> selectByBrandId(String id);

	ShopDetail selectByPrimaryKey(String id);

	List<ShopDetail> selectByShopCity(String brandId);

	List<ShopDetail> selectByCityOrName(String name,String brandId);

	List<ShopDetail> selectByCity(String city,String brandId);

	ShopDetail selectByRestaurantId(Integer restaurantId);

	/**
	 * shopid为饿了么的shopid
	 * @param shopId
	 * @return
     */
	ShopDetail selectByOOrderShopId(Long shopId);

	List<ShopDetail> selectOrderByIndex(String brandId);

	void updateGeekposQueueLoginState(String id,String state);
	
	/**
	 * 查询店铺 排位的基本信息
	 * @author lmx
	 * @version 创建时间：2016年12月13日 下午4:41:19
	 * @param shopId
	 * @return
	 */
	ShopDetail selectQueueInfo(String shopId);

	ShopDetail selectByThirdAppId(String thirdAppid);

	int updateWithDatong(ShopDetail shopDetail,String brandId,String brandName);

	int updatePosWaitredEnvelope(String id,Integer state);

	ShopDetail selectByPosKey(String key);

	/**
	 * 【Pos 2.0】
	 * 根据 shopId 查询 店铺基本信息（包含 brand 信息等设置）
	 * @param shopId
	 * @return
	 */
	ShopDetail posSelectById(String shopId);

	/**
	 * 菜品库根据品牌id、名称查询店铺
	 * */
	List<ShopDetail> selectByShopAndByName(@Param("brandId") String brandId, @Param("name")String name);
}
