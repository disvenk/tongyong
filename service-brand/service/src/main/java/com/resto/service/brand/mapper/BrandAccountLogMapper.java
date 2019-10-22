package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.BrandAccountLog;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;


public interface BrandAccountLogMapper  extends BaseDao<BrandAccountLog,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(BrandAccountLog record);

    int insertSelective(BrandAccountLog record);

    BrandAccountLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BrandAccountLog record);

    int updateByPrimaryKey(BrandAccountLog record);

	/**
	 * yz 2017/08/02
	 * @param beginTime
	 * @param endTime
	 * @param brandId
	 * @return
	 */
	List<BrandAccountLog> selectListByBrandIdAndTime(@Param("beginDate") Date beginTime, @Param("endDate") Date endTime, @Param("brandId") String brandId);


	List<BrandAccountLog> selectListByBrandId(@Param("brandId") String brandId);

	BrandAccountLog selectOneBySerialNumAndBrandId(@Param("serialNumber") String serialNumber, @Param("brandId") String brandId);
}
