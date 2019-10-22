 package com.resto.shop.web.controller.business;

 import com.resto.brand.core.entity.Result;
 import com.resto.shop.web.constant.OrderState;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.model.Order;
 import com.resto.shop.web.model.OrderPaymentItem;
 import com.resto.shop.web.service.OrderPaymentItemService;
 import com.resto.shop.web.service.OrderService;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.annotation.Resource;
 import javax.servlet.http.HttpServletRequest;
 import java.util.List;

 @Controller
 @RequestMapping("orderException")
 public class OrderExceptionController extends GenericController{

     @Resource
     private OrderService orderService;

     @Resource
     private OrderPaymentItemService  orderPaymentItemService;
     /**
      *手动取消已提交但未支付的订单
      * @return
      */
         @RequestMapping("cancelExceptionOrder")
         @ResponseBody
     public Result executeCancelOrder(String beginDate, String endDate, HttpServletRequest request){
             //获取时间
             String begin = request.getParameter("beginDate");
             String end = request.getParameter("endDate");

             //查询所有已提交但未支付的定的那
             List<Order> orderList = orderService.selectNeedCacelOrderList(getCurrentBrandId(),begin,end);
             for(Order order :orderList){
                 if (order.getOrderState() == OrderState.SUBMIT) {
                     System.err.println("自动取消订单:" + order.getId());
                     orderService.cancelExceptionOrder(order.getId());
                 } else {
                     log.info("自动取消订单失败，订单状态不是已提交");
                 }

             }
         return Result.getSuccess();
     }


     /**
      *手动取消没有退款证书的订单
      * @return
      */
     @RequestMapping("cancelNoRootOrder")
     @ResponseBody
     public Result executecancelNoRootOrder(String orderId){
         //查询所有已提交但未支付的定的那
         //更改订单状态为 2，0 和可以取消is_allows=1
         Order o = orderService.selectById(orderId);
         o.setOrderState(2);
         o.setProductionStatus(0);
         o.setAllowCancel(true);
         //查寻orderpaymentItem中result_data 为 {}
        OrderPaymentItem oi =  orderPaymentItemService.selectByOrderIdAndResultData(orderId);
         //删除该订单项
         if(null!=oi){
             orderPaymentItemService.delete(oi.getId());
         }
         orderService.update(o);
         orderService.cancelExceptionOrder(orderId);
         return Result.getSuccess();
     }

     /**
      *单独取消微信支付的钱
      * @return
      */
     @RequestMapping("cancelWXPayOrder")
     @ResponseBody
     public Result executecancelWXPayOrder(String orderId){
         //查询所有已提交但未支付的定的那
         //更改订单状态为 2，0 和可以取消is_allows=1
         Order o = orderService.selectById(orderId);
         o.setOrderState(2);
         o.setProductionStatus(0);
         o.setAllowCancel(true);
         //查寻orderpaymentItem中result_data 为 {}
         OrderPaymentItem oi =  orderPaymentItemService.selectByOrderIdAndResultData(orderId);
         //删除该订单项
         if(null!=oi){
             orderPaymentItemService.delete(oi.getId());
         }
         orderService.update(o);
         orderService.cancelWXPayOrder(orderId);
         return Result.getSuccess();
     }

 }
