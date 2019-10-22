package com.resto.brand.web.service;

import java.util.List;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.BrandUser;

public interface BrandUserService extends GenericService<BrandUser, String> {

	BrandUser selectByUsername(String username);

	BrandUser authentication(BrandUser brandUser);
	
	/**
	 * 添加商家用户
	 */
	Result creatBrandUser(BrandUser brandUser);

	/**
	*@Description:修改商家用户
	*@Author:disvenk.dai
	*@Date:17:19 2018/7/10 0010
	*/
	public Result modifyBrandUser(BrandUser brandUser);
	
	/**
	 * 修改当前用户信息
	 * @param id
	 * @param password
	 */
	void updatePwd(String id,String password);
	
	/** 获取当前登入用户所属商家的所有商家用户
	 * @param currentBrandId
	 * @return
	 */
	List<BrandUser> selectListBybrandId(String currentBrandId);

    /**
     * 查询是品牌管理员的用户
     * @param brandId
     * @return
     */
    BrandUser selectOneByBrandId(String brandId);

    //根据 品牌 ID 和 角色 查询用户信息（选择第一条数据）
    BrandUser selectUserInfoByBrandIdAndRole(String BrandId,int roleId);

    /**
     * 删除用户(假删除)
     * @param id
     * @return
     */
    Void  deleteBrandUser(String id);

	/**
	 * 等位模式登陆
	 * @param username
	 * @param password
     * @return
     */
	BrandUser loginByWaitModel(String username,String password);


	/**
	 * 修改超级密码
	 * @param id
	 * @param password
	 */
	void updateSuperPwd(String id,String password);


	/**
	 * 根据 店铺ID 查询  品牌下所有的管理员信息
	 * Pos2.0 数据拉取接口			By___lmx
	 * @param shopId
	 * @return
	 */
	List<BrandUser> selectByShopId(String shopId);
	BrandUser selectByEmial(String email);
    BrandUser selectByPhone(String phone);
}
