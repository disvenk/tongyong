package com.resto.shop.web.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.resto.shop.web.dto.GrantArticleInfoDto;
import com.resto.shop.web.dto.GrantInfoDto;
import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.OrderItem;

public interface OrderItemMapper  extends GenericDao<OrderItem,String> {
    int deleteByPrimaryKey(String id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
    
    //根据订单ID查询订单项
    List<OrderItem> listByOrderId(Map<String, String> param);

    List<OrderItem> listByOrderIdPos(Map<String, String> param);

    //套餐
    List<OrderItem> listTotalByOrderId(@Param("orderId") String orderId);

    List<OrderItem> listByParentId(String orderId);

    void updateOrderIdByBeforeId(@Param("orderId") String orderId, @Param("beforeId") String beforeId);

	void insertBatch(List<OrderItem> orderItems);

    int selectCountSum(@Param("orderId")String orderId);

    int selectRefundCount(@Param("orderId")String orderId);

    int selectJingCount(@Param("orderId")String orderId);

	/**
	 * 根据时间查询 当前店铺的 菜品销售记录
	 * @param beginDate
	 * @param endDate
	 * @param shopId
	 * @return
	 */
	public List<OrderItem> selectSaleArticleByDate(@Param("beginDate")Date beginDate,@Param("endDate")Date endDate,@Param("shopId")String shopId,@Param("sort")String sort);

	List<OrderItem> listByOrderIds(List<String> childIds);

    List<OrderItem> getListBySort(@Param("parentid") String parentid,@Param("articleid") String articleid);



    
    
    /**
     * 查询订单详情   【用于同步 中间数据库 操作】
     * @param beginDate
     * @param endDate
     * @return
     */
    List<Map<String, Object>> selectOrderItems(@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);

    List<OrderItem> getOrderItemByRecommendId(@Param("recommendId")String recommendId,@Param("orderId")String orderId);

    void refundArticle(@Param("id") String id,@Param("count") Integer count);

    List<OrderItem> selectOrderItemByOrderIds(Map<String, Object> map);

    List<OrderItem> selectOrderItemByOrderId(Map<String, Object> map);
    
    List<OrderItem> selectRefundOrderItem(Map<String, Object> map);

    void refundArticleChild(String parentId);

    /**
     * 得到套餐下的子品
     */
    List<OrderItem> getListByParentId(String parentId);

    List<OrderItem> getListByRecommendId(@Param("recommendId") String recommendId,@Param("orderId") String orderId);

    List<OrderItem> selectRefundArticleItem(@Param("orderId") String orderId);

    List<OrderItem> selectByArticleIds(String[] articleIds);

    /**
     * 根据 订单ID 删除
     * Pos 2.0 同步数据使用
     * @param orderId
     */
    void posSyncDeleteByOrderId(String orderId);

    List<OrderItem> getOrderBefore(@Param("tableNumber") String tableNumber,
                                   @Param("shopId") String shopId,@Param("customerId") String customerId);

    List<OrderItem> posSyncListByOrderId(@Param("orderId") String orderId);

    List<OrderItem> listArticle(@Param("orderId") String orderId);

    /**
     * 插入或者更新
     * @param orderItem
     */
    public void updateOrinsertOrderItem(OrderItem orderItem);

    /**
     * 根据订单查询订单项
     * @param orderId
     * @return
     */
    List<OrderItem> quertItemByOrderId(@Param("orderId") String orderId);

    void insertOrUpdateOrderItems(List<OrderItem> orderItemList);

    List<OrderItem> selectParentItemByOrderId(String orderId);
    List<GrantArticleInfoDto> grantArticleList(@Param("beginDate")String beginDate,@Param("endDate")String endDate);

    String selectFamilyName(@Param("id")String id);

    List<String> selectRefundRemark(@Param("articleId")String articleId,@Param("orderId")String orderId);

    GrantInfoDto getGrantInfo(@Param("id")String id);
}
