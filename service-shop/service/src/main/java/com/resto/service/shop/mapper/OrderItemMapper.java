package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderItemMapper extends BaseDao<OrderItem,String> {

    int insert(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
    
    //根据订单ID查询订单项
    List<OrderItem> listByOrderId(@Param("orderId") String orderId);

    //套餐
    List<OrderItem> listTotalByOrderId(@Param("orderId") String orderId);


    List<OrderItem> listByParentId(String orderId);

	void insertBatch(List<OrderItem> orderItems);

	
	/**
	 * 根据时间查询 当前店铺的 菜品销售记录
	 * @param beginDate
	 * @param endDate
	 * @param shopId
	 * @return
	 */
	public List<OrderItem> selectSaleArticleByDate(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate, @Param("shopId") String shopId, @Param("sort") String sort);

	List<OrderItem> listByOrderIds(List<String> childIds);

    List<OrderItem> getListBySort(@Param("parentid") String parentid, @Param("articleid") String articleid);





    /**
     * 查询订单详情   【用于同步 中间数据库 操作】
     * @param beginDate
     * @param endDate
     * @return
     */
    List<Map<String, Object>> selectOrderItems(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    List<OrderItem> getOrderItemByRecommendId(@Param("recommendId") String recommendId, @Param("orderId") String orderId);

    void refundArticle(@Param("id") String id, @Param("count") Integer count);

    List<OrderItem> selectOrderItemByOrderIds(Map<String, Object> map);

    List<OrderItem> selectOrderItemByOrderId(Map<String, Object> map);

    List<OrderItem> selectRefundOrderItem(Map<String, Object> map);

    void refundArticleChild(String parentId);

    /**
     * 得到套餐下的子品
     */
    List<OrderItem> getListByParentId(String parentId);

    List<OrderItem> getListByRecommendId(@Param("recommendId") String recommendId, @Param("orderId") String orderId);

    List<OrderItem> selectRefundArticleItem(@Param("orderId") String orderId);

    List<OrderItem> selectByArticleIds(String[] articleIds);
}
