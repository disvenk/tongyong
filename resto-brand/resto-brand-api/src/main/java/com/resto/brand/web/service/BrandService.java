package com.resto.brand.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.Brand;

public interface BrandService extends GenericService<Brand, String> {

	/**
	 * 根据BrandId 查询品牌信息
	 * @param brandId
	 * @return
	 */
	Brand selectByPrimaryKey(String brandId);

	/**
	 * 查询所有状态为1的品牌详细信息，包含该品牌的数据库配置和微信配置
	 * @return
	 */
	List<Brand> selectBrandDetailInfo();
	
	/**
	 * 添加 品牌 信息（包括关联的表）
	 * @param brand
	 * @return
	 */
	void insertInfo(Brand brand);
	
	/**
	 * 修改 品牌 信息（包括关联的表）
	 * @param brand
	 */
	void updateInfo(Brand brand);
	
	/**
	 * 删除信息
	 * @param brandId
	 */
	void deleteInfo(String brandId);
	
	/**
	 * 查询 品牌 信息 和 该品牌旗下的店铺
	 * @return
	 */
	List<Brand> queryBrandAndShop();

	Brand selectBySign(String brandSign);

	/**
	 * 验证品牌的标识是否重复
	 * @param brandId
	 * @param brandSign
	 * @return
	 */
	boolean validataBrandInfo(String brandSign,String brandId);

	/**
	 * 根据品牌设置查找品牌
	 */
	Brand selectBrandBySetting(String settingId);

}
