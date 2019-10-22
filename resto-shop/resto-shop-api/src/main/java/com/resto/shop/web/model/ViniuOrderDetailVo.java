package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ViniuOrderDetailVo implements Serializable {

    //序列  1
    private Integer row_id;

    //订单模式
    private Integer order_mode;

    //桌号
    private String table_num;

    //就餐人数
    private Integer guests_nums;

    //就餐类型  1
    private String eat_type;

    //就餐类型编码  1
    private String eat_typecode;

    //订单来源  1
    private String order_from;

    //订单来源编码  1
    private String order_fromcode;

    //用户支付金额
    private BigDecimal user_pay;

    //商品总金额
    private BigDecimal product_price;

    //押金  1
    private BigDecimal deposit_price;

    //附加金额  1
    private BigDecimal additional_price;

    //应收金额
    private BigDecimal yingshou_price;

    //实收金额
    private BigDecimal shishou_price;

    //折扣金额
    private BigDecimal zhekou_price;

    //四舍五入
    private BigDecimal round_price;

    //抹零
    private BigDecimal removezero_price;

    //溢收金额
    private BigDecimal earn_price;

    //服务费
    private BigDecimal thirdServiceCharge;

    //补贴
    private BigDecimal thirdSubsidies;

    //订单总价
    private BigDecimal order_price;

    //服务员
    private String waiter_name;

    //操作员
    private String operator_name;

    //订单时间
    private String order_time;

    //订单号
    private String order_no;

    //支付时间
    private String pay_time;

    //订单类型  1
    private Integer order_type;

    //用餐用户姓名
    private String order_user_name;

    //用餐用户电话
    private String order_user_tel;

    //外卖用户名称
    private String wm_user;

    //外卖用户联系方式
    private String wm_tel;

    //外卖用户收货地址
    private String wm_addr;

    //外卖平台
    private String wm_plat;

    //押金明细
   private List<DepositData> depositData;

    //菜品明细
    private List<FoodData> foodData;

    //附加费明细
    private List<AdditionalData> additionalData;

    //实收明细
    private List<SaleData> sale_data;

    //折扣明细
    private List<DiscountData> discount_data;

    public Integer getRow_id() {
        return row_id;
    }

    public void setRow_id(Integer row_id) {
        this.row_id = row_id;
    }

    public Integer getOrder_mode() {
        return order_mode;
    }

    public void setOrder_mode(Integer order_mode) {
        this.order_mode = order_mode;
    }

    public String getTable_num() {
        return table_num;
    }

    public void setTable_num(String table_num) {
        this.table_num = table_num;
    }

    public Integer getGuests_nums() {
        return guests_nums;
    }

    public void setGuests_nums(Integer guests_nums) {
        this.guests_nums = guests_nums;
    }

    public String getEat_type() {
        return eat_type;
    }

    public void setEat_type(String eat_type) {
        this.eat_type = eat_type;
    }

    public String getEat_typecode() {
        return eat_typecode;
    }

    public void setEat_typecode(String eat_typecode) {
        this.eat_typecode = eat_typecode;
    }

    public String getOrder_from() {
        return order_from;
    }

    public void setOrder_from(String order_from) {
        this.order_from = order_from;
    }

    public String getOrder_fromcode() {
        return order_fromcode;
    }

    public void setOrder_fromcode(String order_fromcode) {
        this.order_fromcode = order_fromcode;
    }

    public BigDecimal getUser_pay() {
        return user_pay;
    }

    public void setUser_pay(BigDecimal user_pay) {
        this.user_pay = user_pay;
    }

    public BigDecimal getProduct_price() {
        return product_price;
    }

    public void setProduct_price(BigDecimal product_price) {
        this.product_price = product_price;
    }

    public BigDecimal getDeposit_price() {
        return deposit_price;
    }

    public void setDeposit_price(BigDecimal deposit_price) {
        this.deposit_price = deposit_price;
    }

    public BigDecimal getAdditional_price() {
        return additional_price;
    }

    public void setAdditional_price(BigDecimal additional_price) {
        this.additional_price = additional_price;
    }

    public BigDecimal getYingshou_price() {
        return yingshou_price;
    }

    public void setYingshou_price(BigDecimal yingshou_price) {
        this.yingshou_price = yingshou_price;
    }

    public BigDecimal getShishou_price() {
        return shishou_price;
    }

    public void setShishou_price(BigDecimal shishou_price) {
        this.shishou_price = shishou_price;
    }

    public BigDecimal getZhekou_price() {
        return zhekou_price;
    }

    public void setZhekou_price(BigDecimal zhekou_price) {
        this.zhekou_price = zhekou_price;
    }

    public BigDecimal getRound_price() {
        return round_price;
    }

    public void setRound_price(BigDecimal round_price) {
        this.round_price = round_price;
    }

    public BigDecimal getRemovezero_price() {
        return removezero_price;
    }

    public void setRemovezero_price(BigDecimal removezero_price) {
        this.removezero_price = removezero_price;
    }

    public BigDecimal getEarn_price() {
        return earn_price;
    }

    public void setEarn_price(BigDecimal earn_price) {
        this.earn_price = earn_price;
    }

    public BigDecimal getThirdServiceCharge() {
        return thirdServiceCharge;
    }

    public void setThirdServiceCharge(BigDecimal thirdServiceCharge) {
        this.thirdServiceCharge = thirdServiceCharge;
    }

    public BigDecimal getThirdSubsidies() {
        return thirdSubsidies;
    }

    public void setThirdSubsidies(BigDecimal thirdSubsidies) {
        this.thirdSubsidies = thirdSubsidies;
    }

    public BigDecimal getOrder_price() {
        return order_price;
    }

    public void setOrder_price(BigDecimal order_price) {
        this.order_price = order_price;
    }

    public String getWaiter_name() {
        return waiter_name;
    }

    public void setWaiter_name(String waiter_name) {
        this.waiter_name = waiter_name;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public Integer getOrder_type() {
        return order_type;
    }

    public void setOrder_type(Integer order_type) {
        this.order_type = order_type;
    }

    public String getOrder_user_name() {
        return order_user_name;
    }

    public void setOrder_user_name(String order_user_name) {
        this.order_user_name = order_user_name;
    }

    public String getOrder_user_tel() {
        return order_user_tel;
    }

    public void setOrder_user_tel(String order_user_tel) {
        this.order_user_tel = order_user_tel;
    }

    public String getWm_user() {
        return wm_user;
    }

    public void setWm_user(String wm_user) {
        this.wm_user = wm_user;
    }

    public String getWm_tel() {
        return wm_tel;
    }

    public void setWm_tel(String wm_tel) {
        this.wm_tel = wm_tel;
    }

    public String getWm_addr() {
        return wm_addr;
    }

    public void setWm_addr(String wm_addr) {
        this.wm_addr = wm_addr;
    }

    public String getWm_plat() {
        return wm_plat;
    }

    public void setWm_plat(String wm_plat) {
        this.wm_plat = wm_plat;
    }

    public List<DepositData> getDepositData() {
        return depositData;
    }

    public void setDepositData(List<DepositData> depositData) {
        this.depositData = depositData;
    }

    public List<FoodData> getFoodData() {
        return foodData;
    }

    public void setFoodData(List<FoodData> foodData) {
        this.foodData = foodData;
    }

    public List<AdditionalData> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(List<AdditionalData> additionalData) {
        this.additionalData = additionalData;
    }

    public List<SaleData> getSale_data() {
        return sale_data;
    }

    public void setSale_data(List<SaleData> sale_data) {
        this.sale_data = sale_data;
    }

    public List<DiscountData> getDiscount_data() {
        return discount_data;
    }

    public void setDiscount_data(List<DiscountData> discount_data) {
        this.discount_data = discount_data;
    }
}
