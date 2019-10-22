package com.resto.brand.web.dao;

import org.apache.ibatis.annotations.Param;
import com.resto.brand.web.model.BrandUser;

import java.util.List;

import com.resto.brand.core.generic.GenericDao;

public interface BrandUserMapper  extends GenericDao<BrandUser,String> {
    int deleteByPrimaryKey(String id);

    int insert(BrandUser record);

    int insertSelective(BrandUser record);

    BrandUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BrandUser record);

    int updateByPrimaryKey(BrandUser record);

	BrandUser selectByUsername(String username);

	BrandUser authentication(BrandUser branduser);
	/**
	 * 修改当前用户密码
	 * @param id
	 * @param password
	 */
	void updatePwd(@Param("id") String id,@Param("password")String password);

    void updateSuperPwd(@Param("id") String id,@Param("password")String password);

	List<BrandUser> selectListBybrandId(String shopId);

	BrandUser selectOneByBrandId(String brandId);

    BrandUser selectUserInfoByBrandIdAndRole(@Param("brandId") String brandId, @Param("roleId") int roleId);

	BrandUser loginByWaitModel(@Param("username") String userName,@Param("password") String password);


	/**
	 * 根据 店铺ID 查询  品牌下所有的管理员信息
	 * Pos2.0 数据拉取接口			By___lmx
	 * @param shopId
	 * @return
	 */
	List<BrandUser> selectByShopId(@Param("shopId") String shopId);

    BrandUser selectByPhone(@Param("phone") String phone);

	BrandUser selectByEmail(@Param("email")String email);
}
