package com.resto.shop.web.service;

import com.resto.shop.web.model.OffLineOrder;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.posDto.ArticleSupport;
import com.resto.shop.web.posDto.ShopMsgChangeDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by KONATA on 2017/8/9.
 */
public interface PosService {
    /**
     * 同步店铺菜品库存 (服务器->pos)
     * @param shopId 店铺id
     * @return
     */
    String syncArticleStock(String shopId);


    /**
     * 当门店后台数据发生变更时通知pos （服务器->pos）
     * @return 发生信息变更的门店
     */
    void shopMsgChange(ShopMsgChangeDto shopMsgChangeDto);

    /**
     * 同步订单创建时的订单信息 （服务器->pos）
     * @param orderId
     * @return
     */
    String syncOrderCreated(String orderId);

    /**
     * 同步订单支付时的信息 （服务器 ->pos）
     * @param orderId
     * @return
     */
    String syncOrderPay(String orderId);

    /**
     * 同步第三方外卖数据（服务器->pos）
     * @param orderId 第三方外卖id
     */
    String syncPlatform(String orderId);

    /**
     * 菜品上架、下架
     * @param articleId 菜品id
     * @param actived 0 下架  1 上架
     * @return
     */
    void articleActived(String articleId,Integer actived);

    /**
     * 菜品沽清
     * @param articleId 菜品id
     */
    String articleEmpty(String articleId);

    /**
     * 编辑菜品库存
     * @param articleId 菜品id
     * @param count 数量
     */
    void articleEdit(String articleId,Integer count);

    /**
     * 打印成功
     * @param orderId 订单
     */
    void printSuccess(String orderId);

    /**
     * 同步pos端创建的订单
     * @param data json数据
     */
    String syncPosCreateOrder(String data);


    /**
     * 同步pos端订单支付信息
     * @param data json数据
     */
    String syncPosOrderPay(String data);

    /**
     * 同步pos端退菜信息
     * @param data json数据
     */
    String syncPosRefundOrder(String data);

    /**
     * 同步pos端赠菜信息
     * @param data json数据
     */
    String syncPosGrantOrder(String data);


    /**
     * pos确认订单
     * @param orderId 订单id
     */
    void syncPosConfirmOrder(String orderId);

    /**
     * 同步菜品供应时间
     * @return
     */
    List<ArticleSupport>  syncArticleSupport(String shopId);

    String syncChangeTable(String orderId,String tableNumber);

    /**
     * 开台，将桌位设置为占用状态
     * @param shopId
     * @param tableNumber
     */
    void syncOpenTable(String shopId,String tableNumber);

    /**
     * 更新桌位状态（空闲或者锁定）
     * @param shopId
     * @param tableNumber
     * @param state  false：锁定   true：释放
     */
    String syncTableState(String shopId,String tableNumber,boolean state, String orderId);

    /**
     * 更新桌位状态（空闲或者锁定）
     * @param shopId
     * @param tableNumber
     * @param state  false：锁定   true：释放
     */
    String syncTableState(String shopId,String tableNumber,boolean state);

    /**
     * 同步 Pos 端 本地订单
     * @param data
     */
    boolean syncPosLocalOrder(String data);

    /**
     * Pos端 结店操作
     * @param brandId
     * @param shopId
     * @param offLineOrder
     */
    void posCheckOut(String brandId,String shopId, OffLineOrder offLineOrder,String operator,String dailyLogId);

    /**
     * Pos端 取消订单  目前只支持，未支付的订单
     * @param shopId
     * @param orderId
     */
    void posCancelOrder(String shopId, String orderId);

    /**
     *  服务器异常，设置店铺不在线，不允许微信端下单
     * @param brandId
     * @param shopId
     */
    void serverError(String brandId,String shopId);

    /**
     * 发送虚拟消息队列数据
     * @param shopId  店铺ID
     * @param type  消息类型
     * @param orderId   订单ID
     * @param platformType   外卖平台类型
     */
    void sendMockMQMessage(String shopId, String type, String orderId, Integer platformType);

    /**
     * 发送指定命令至Pos端
     * @param shopId
     * @param type
     * @param sql
     */
    void sendServerCommand(String shopId, String type, String sql);

    /**
     * 获取服务器中的有效订单ID（非取消订单）
     * @param shopId
     * @return
     */
    List<String> getServerOrderIds(String shopId);


    /**
     * Pos 叫号
     * @param orderId
     */
    void posCallNumber(String orderId);

    /**
     * pos端手动打印订单
     * 此功能用于 手动改变订单状态为 已打印
     * @param orderId
     */
    void posPrintOrder(String orderId);

    /**
     * 获取该店铺，当天的异常订单
     * @param shopId
     * @return
     */
    String serverExceptionOrderList(String shopId);

    void test();

    /**
     * 扫码支付构建微信请求支付接口
     * @param data
     * @return
     */
    String scanCodePayment(String data);

    /**
     * 查询扫码支付订单的情况
     * @param data
     * @return
     */
    String confirmPayment(String data);

    /**
     * 撤销扫码支付的订单
     * @param data
     * @return
     */
    String revocationOfOrder(String data);

    /**
     * 动态修改业务数据
     * @param data
     * @return
     */
    String updateData(String data);

    /**
     * 得到能用的余额和优惠券
     * @param orderId
     * @return
     */
    String useAccountAndCoupon(String orderId);

    /**
     * 同步数据接口
     * @param order
     */
    void synchronousData(Order order);

    /**
     * 移除优惠券
     * @param couponId
     */
    void removeIsUseCoupon(String couponId);

    /**
     * 根据用户Id、固定金额获取用户余额
     * @param data
     * @return
     */
    String getCustomerAmount(String data);
}
