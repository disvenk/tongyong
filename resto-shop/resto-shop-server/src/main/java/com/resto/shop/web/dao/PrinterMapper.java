package com.resto.shop.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.Printer;

public interface PrinterMapper  extends GenericDao<Printer,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(Printer record);

    int insertSelective(Printer record);

    Printer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Printer record);

    int updateByPrimaryKey(Printer record);
    
    /**
     * 根据店铺ID查询信息
     * @param currentShopId
     * @return
     */
    List<Printer> selectListByShopId(@Param(value = "shopId") String currentShopId);
    
    /**
     * 根据店铺Id和打印机类型查询
     * @param id
     * @param reception
     * @return
     */

	List<Printer> selectByShopAndType(@Param(value = "shopDetailId")String id,@Param(value = "printType") int reception);


    Integer checkError(@Param(value = "shopId") String currentShopId);

    List<Printer> selectQiantai(@Param("shopId") String shopId,@Param("type") Integer type);

    Printer getCashPrinter(String shopId);

    List<Printer> selectTicketNotSame(String shopId);

    List<Printer> selectLabelNotSame(String shopId);

    List<Printer> selectPrintByType(@Param("shopId") String shopId, @Param("type") Integer type);

    /**
     * 联动修改别的该ip打印机的备用打印机ip
     * @param ip
     * @param spareIp
     */
    void updateSpareIpByIp(@Param("ip") String ip, @Param("spareIp") String spareIp);
}
