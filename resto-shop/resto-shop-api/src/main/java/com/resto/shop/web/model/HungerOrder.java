package com.resto.shop.web.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by KONATA on 2016/10/31.
 * 饿了吗订单
 */
public class HungerOrder implements Serializable {

    //主键
    private String id;
    //送货地址
    private String address;
    //送货人姓名
    private String consignee;
    //创建时间
    private Date createdAt;
    //配送费
    private BigDecimal deliverFee;
    //送达时间
    private Date deliverTime;
    //订单备注
    private String description;
    //是否开发票（1：是  0否）
    private Integer invoice;
    //是否预定单（1 是  0 否）
    private Integer isBook;
    //是否在线支付 （1 是 0 否）
    private Integer isOnlinePaid;
    //饿了吗订单id
    private String orderId;
    //顾客联系电话
    private String phoneList;
    //饿了吗餐厅id
    private Integer restaurantId;
    //饿了吗餐厅名称
    private String restaurantName;
    //餐厅数量
    private Integer restaurantNumber;
    //订单状态(-5 等待支付 -4 支付失败 -1 已取消 0 未处理 2 已处理 11 已确认)
    private Integer statusCode;
    //订单总价(减去优惠后的价格)
    private BigDecimal totalPrice;
    //菜价加上配送费和打包费的价格
    private BigDecimal originalPrice;
    //饿了吗用户id
    private Integer userId;
    //饿了吗用户名称
    private String userName;
    //订单收货地址经纬度，例如：31.2538,121.4185
    private String deliveryGeo;
    //送货地址
    private String deliveryPoiAddress;

    //菜品总数
    private Integer sum;

    private Integer detailCount;

    private Integer type;
    
    public Integer getDetailCount() {
		return detailCount;
	}

	public void setDetailCount(Integer detailCount) {
		this.detailCount = detailCount;
	}
	//额外费用
    private List<HungerOrderExtra> extra;

    //饿了吗订单明细分组
    private List<HungerOrderGroup> groups;

    private List<HungerOrderDetail> details;

    private String shopName;

    private String shopDetailId;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    final public String getShopName() {
        return shopName;
    }

    final public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    final public Integer getSum() {
        return sum;
    }

    final public void setSum(Integer sum) {
        this.sum = sum;
    }

    final public List<HungerOrderDetail> getDetails() {
        return details;
    }

    final public void setDetails(List<HungerOrderDetail> details) {
        this.details = details;
    }

    final public List<HungerOrderGroup> getGroups() {
        return groups;
    }

    final public void setGroups(List<HungerOrderGroup> groups) {
        this.groups = groups;
    }

    final public List<HungerOrderExtra> getExtra() {
        return extra;
    }

    final public void setExtra(List<HungerOrderExtra> extra) {
        this.extra = extra;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    final public String getAddress() {
        return address;
    }

    final public void setAddress(String address) {
        this.address = address;
    }

    final public String getConsignee() {
        return consignee;
    }

    final public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    final public Date getCreatedAt() {
        return createdAt;
    }

    final public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    final public BigDecimal getDeliverFee() {
        return deliverFee;
    }

    final public void setDeliverFee(BigDecimal deliverFee) {
        this.deliverFee = deliverFee;
    }

    final public Date getDeliverTime() {
        return deliverTime;
    }

    final public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    final public String getDescription() {
        return description;
    }

    final public void setDescription(String description) {
        this.description = description;
    }

    final public Integer getInvoice() {
        return invoice;
    }

    final public void setInvoice(Integer invoice) {
        this.invoice = invoice;
    }

    final public Integer getIsBook() {
        return isBook;
    }

    final public void setIsBook(Integer isBook) {
        this.isBook = isBook;
    }

    final public Integer getIsOnlinePaid() {
        return isOnlinePaid;
    }

    final public void setIsOnlinePaid(Integer isOnlinePaid) {
        this.isOnlinePaid = isOnlinePaid;
    }

    final public String getOrderId() {
        return orderId;
    }

    final public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    final public String getPhoneList() {
        return phoneList;
    }

    final public void setPhoneList(String phoneList) {
        this.phoneList = phoneList;
    }

    final public Integer getRestaurantId() {
        return restaurantId;
    }

    final public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    final public String getRestaurantName() {
        return restaurantName;
    }

    final public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    final public Integer getRestaurantNumber() {
        return restaurantNumber;
    }

    final public void setRestaurantNumber(Integer restaurantNumber) {
        this.restaurantNumber = restaurantNumber;
    }

    final public Integer getStatusCode() {
        return statusCode;
    }

    final public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    final public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    final public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    final public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    final public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    final public Integer getUserId() {
        return userId;
    }

    final public void setUserId(Integer userId) {
        this.userId = userId;
    }

    final public String getUserName() {
        return userName;
    }

    final public void setUserName(String userName) {
        this.userName = userName;
    }

    final public String getDeliveryGeo() {
        return deliveryGeo;
    }

    final public void setDeliveryGeo(String deliveryGeo) {
        this.deliveryGeo = deliveryGeo;
    }

    final public String getDeliveryPoiAddress() {
        return deliveryPoiAddress;
    }

    final public void setDeliveryPoiAddress(String deliveryPoiAddress) {
        this.deliveryPoiAddress = deliveryPoiAddress;
    }

    public HungerOrder() {}
    public HungerOrder(JSONObject object) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        address = object.optString("address");
        consignee = object.optString("consignee");
        if(!StringUtils.isEmpty(object.optString("created_at"))){
            createdAt = sdf.parse(object.optString("created_at"));
        }

        deliverFee = object.optBigDecimal("deliver_fee",new BigDecimal(0));
        if(!StringUtils.isEmpty(object.optString("deliver_time"))){
            deliverTime = sdf.parse(object.optString("deliver_time"));
        }

        description = object.optString("description");
        invoice = object.optInt("invoice");
        isBook = object.optInt("is_book");
        isOnlinePaid = object.optInt("is_online_paid");
        orderId = object.optString("order_id");
        phoneList = object.optString("phone_list");
        restaurantId = object.optInt("restaurant_id");
        restaurantName = object.optString("restaurant_name");
        restaurantName = object.optString("restaurant_number");
        statusCode = object.optInt("status_code");
        totalPrice = object.optBigDecimal("total_price",new BigDecimal(0));
        originalPrice = object.optBigDecimal("original_price",new BigDecimal(0));
        userId = object.optInt("user_id");
        userName = object.optString("user_name");
        deliveryGeo = object.optString("delivery_geo");
        deliveryPoiAddress = object.optString("delivery_poi_address");

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("address", address)
                .append("consignee", consignee)
                .append("createdAt", createdAt)
                .append("deliverFee", deliverFee)
                .append("deliverTime", deliverTime)
                .append("description", description)
                .append("invoice", invoice)
                .append("isBook", isBook)
                .append("isOnlinePaid", isOnlinePaid)
                .append("orderId", orderId)
                .append("phoneList", phoneList)
                .append("restaurantId", restaurantId)
                .append("restaurantName", restaurantName)
                .append("restaurantNumber", restaurantNumber)
                .append("statusCode", statusCode)
                .append("totalPrice", totalPrice)
                .append("originalPrice", originalPrice)
                .append("userId", userId)
                .append("userName", userName)
                .append("deliveryGeo", deliveryGeo)
                .append("deliveryPoiAddress", deliveryPoiAddress)
                .append("sum", sum)
                .append("detailCount", detailCount)
                .append("extra", extra)
                .append("groups", groups)
                .append("details", details)
                .append("shopName", shopName)
                .append("shopDetailId", shopDetailId)
                .toString();
    }
}
