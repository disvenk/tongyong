package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.GetNumber;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by carl on 2016/10/14.
 */
public interface GetNumberMapper  extends GenericDao<GetNumber,String> {

    int insert(GetNumber getNumber);

    int insertSelective(GetNumber getNumber);

    int updateByPrimaryKeySelective(GetNumber getNumber);

    int updateByPrimaryKey(GetNumber getNumber);

    List<GetNumber> selectByTableTypeShopId(@Param("tableType")String tableType,@Param("shopId")String shopId);

    GetNumber selectByPrimaryKey(String id);

    List<GetNumber> selectCount(@Param("tableType") String tableType,@Param("date") Date date, @Param("shopId") String shopId);

    GetNumber getWaitInfoByCustomerId(@Param("customerId") String customerId,@Param("shopId") String shopId,@Param("timeOut") Integer timeOut);

    Integer getWaitNumber(GetNumber getNumber);

    GetNumber selectGetNumberInfo(String id);

    List<GetNumber> selectWaitCountByCodeId(@Param("shopId") String shopId, @Param("codeId") String codeId);

    List<GetNumber> selectBeforeNumberByCodeId(@Param("shopId") String shopId, @Param("codeId") String codeId, @Param("time") Date time);

    List<GetNumber> selectAfterNumberByCodeId(@Param("shopId") String shopId, @Param("codeId") String codeId, @Param("time") Date time);

    GetNumber selectNowNumberByCodeId(@Param("shopId") String shopId, @Param("codeId") String codeId);
}
