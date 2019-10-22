package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.AccountAddressInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AccountAddressInfoMapper  extends BaseDao<AccountAddressInfo,String> {
    int deleteByPrimaryKey(String id);

    int insert(AccountAddressInfo record);

    int insertSelective(AccountAddressInfo record);

    AccountAddressInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountAddressInfo record);

    int updateByPrimaryKey(AccountAddressInfo record);

	/**
	 * yz 2017/08/01 查询品牌账户发票地址
	 * @param brandId
	 * @return
	 */
	List<AccountAddressInfo> selectByBrandId(@Param("brandId") String brandId);
}
