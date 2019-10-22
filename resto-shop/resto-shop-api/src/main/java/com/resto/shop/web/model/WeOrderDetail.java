package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class WeOrderDetail implements Serializable {
    private Long id;

    private String shopId;

    private String shopName;

    private String brandName;

    private Date createTime;

    private BigDecimal restoTotal;

    private Integer restoCount;

    private BigDecimal enterTotal;

    private Integer enterCount;

    private Integer customerCount;

    private BigDecimal avgCustomerTotal;

    private Integer wechatCount;

    private BigDecimal wechatTotal;

    private Integer alipayCount;

    private BigDecimal alipayTotal;

    private Integer cashCount;

    private BigDecimal cashTotal;

    private Integer bankCount;

    private BigDecimal bankTotal;

    private Integer chargeCount;

    private BigDecimal chargeTotal;

    private Integer redCount;

    private BigDecimal redTotal;

    private Integer couponCount;

    private BigDecimal couponTotal;

    private Integer waitCount;

    private BigDecimal waitTotal;

    private Integer chargeReturnCount;

    private BigDecimal chargeReturnTotal;

    private Integer returnCount;

    private BigDecimal returnTotal;

    private Integer orderItemCount;

    private BigDecimal orderItemTotal;

    private BigDecimal returnItemTotal;

    private Integer returnItemCount;

    private BigDecimal returnCustomerTotal;

    private Integer returnCustomerCount;

    private BigDecimal newNormalCustomerTotal;

    private Integer newNormalCustomerCount;

    private BigDecimal newCustomerTotal;

    private Integer newCustomerCount;

    private Integer newShareCustomerCount;

    private BigDecimal newShareCustomerTotal;

    private Integer backCustomerCount;

    private BigDecimal backCustomerToatal;

    private Integer backTwoCustomerCount;

    private BigDecimal backTwoCustomerTotal;

    private Integer backTwoMoreCustomerCount;

    private BigDecimal backTwoMoreCustomerTotal;

    private String sAvgScore;

    private BigDecimal sRestoTotal;

    private BigDecimal sEnterTotal;

    private BigDecimal sAllTotal;

    private BigDecimal sTotalIncome;

    private BigDecimal sPayTotal;

    private Integer sNewCustomr;

    private BigDecimal sNewCustomerTotal;

    private Integer sNewNormalCustomer;

    private BigDecimal sNewNormalCustomerTotal;

    private Integer sNewShareCustomer;

    private BigDecimal sNewShareCustomerTotal;

    private Integer sBackCustomer;

    private BigDecimal sBackCustomerTotal;

    private Integer sBackTwoCustomer;

    private BigDecimal sBackTwoCustomerTotal;

    private Integer sBackTwoMoreCustomer;

    private BigDecimal sBackTwoMoreCustomerTotal;

    private String zAvgScore;

    private BigDecimal zRestoTotal;

    private BigDecimal zEnterTotal;

    private BigDecimal zTotalIncome;

    private BigDecimal zAllTotal;

    private BigDecimal zPayTotal;

    private BigDecimal zNewCustomerTotal;

    private Integer zNewCustomerCount;

    private Integer zNewNormalCustomer;

    private BigDecimal zNewNormalCustomerTotal;

    private Integer zNewShareCustomer;

    private BigDecimal zNewShareCustomerTotal;

    private Integer zBackCustomer;

    private BigDecimal zBackCustomerTotal;

    private Integer zBackTwoCustomer;

    private BigDecimal zBackTwoCustomerTotal;

    private Integer zBackTwoMoreCustomer;

    private BigDecimal zBackTwoMoreCustomerTotal;

    private String xAvgScore;

    private BigDecimal xRestoTotal;

    private BigDecimal xEnterTotal;

    private BigDecimal xAllTotal;

    private BigDecimal xTotalIncome;

    private BigDecimal xPayTotal;

    private BigDecimal xNewCustomerTotal;

    private Integer xNewNormalCustomer;

    private Integer xNewCustomer;

    private BigDecimal xNewNormalCustomerTotal;

    private Integer xNewShareCustomer;

    private BigDecimal xNewShareCustomerTotal;

    private Integer xBackCustomer;

    private BigDecimal xBackCustomerTotal;

    private Integer xBackTwoCustomer;

    private BigDecimal xBackTwoCustomerTotal;

    private Integer xBackTwoMoreCustomer;

    private BigDecimal xBackTwoMoreCustomerTotal;

    private String mAvgScore;

    private BigDecimal mRestoTotal;

    private BigDecimal mEnterTotal;

    private BigDecimal mAllTotal;

    private BigDecimal mTotalIncome;

    private BigDecimal mPayTotal;

    private Integer mNewCustomr;

    private BigDecimal mNewCustomerTotal;

    private Integer mNewNormalCustomer;

    private BigDecimal mNewNormalCustomerTotal;

    private Integer mNewShareCustomer;

    private BigDecimal mNewShareCustomerTotal;

    private Integer mBackCustomer;

    private BigDecimal mBackCustomerTotal;

    private Integer mBackTwoCustomer;

    private BigDecimal mBackTwoCustomerTotal;

    private Integer mBackTwoMoreCustomer;

    private BigDecimal mBackTwoMoreCustomerTotal;


    private List<WeChargeLog> weChargeLogList;

    private  List<WeReturnItem> weReturnItemList;

    private  List<WeItem> weItemList;

    private List<WeReturnCustomer> weReturnCustomerList;

    public List<WeChargeLog> getWeChargeLogList() {
        return weChargeLogList;
    }

    public void setWeChargeLogList(List<WeChargeLog> weChargeLogList) {
        this.weChargeLogList = weChargeLogList;
    }

    public List<WeReturnItem> getWeReturnItemList() {
        return weReturnItemList;
    }

    public void setWeReturnItemList(List<WeReturnItem> weReturnItemList) {
        this.weReturnItemList = weReturnItemList;
    }

    public List<WeItem> getWeItemList() {
        return weItemList;
    }

    public void setWeItemList(List<WeItem> weItemList) {
        this.weItemList = weItemList;
    }

    public List<WeReturnCustomer> getWeReturnCustomerList() {
        return weReturnCustomerList;
    }

    public void setWeReturnCustomerList(List<WeReturnCustomer> weReturnCustomerList) {
        this.weReturnCustomerList = weReturnCustomerList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getRestoTotal() {
        return restoTotal;
    }

    public void setRestoTotal(BigDecimal restoTotal) {
        this.restoTotal = restoTotal;
    }

    public Integer getRestoCount() {
        return restoCount;
    }

    public void setRestoCount(Integer restoCount) {
        this.restoCount = restoCount;
    }

    public BigDecimal getEnterTotal() {
        return enterTotal;
    }

    public void setEnterTotal(BigDecimal enterTotal) {
        this.enterTotal = enterTotal;
    }

    public Integer getEnterCount() {
        return enterCount;
    }

    public void setEnterCount(Integer enterCount) {
        this.enterCount = enterCount;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    public BigDecimal getAvgCustomerTotal() {
        return avgCustomerTotal;
    }

    public void setAvgCustomerTotal(BigDecimal avgCustomerTotal) {
        this.avgCustomerTotal = avgCustomerTotal;
    }

    public Integer getWechatCount() {
        return wechatCount;
    }

    public void setWechatCount(Integer wechatCount) {
        this.wechatCount = wechatCount;
    }

    public BigDecimal getWechatTotal() {
        return wechatTotal;
    }

    public void setWechatTotal(BigDecimal wechatTotal) {
        this.wechatTotal = wechatTotal;
    }

    public Integer getAlipayCount() {
        return alipayCount;
    }

    public void setAlipayCount(Integer alipayCount) {
        this.alipayCount = alipayCount;
    }

    public BigDecimal getAlipayTotal() {
        return alipayTotal;
    }

    public void setAlipayTotal(BigDecimal alipayTotal) {
        this.alipayTotal = alipayTotal;
    }

    public Integer getCashCount() {
        return cashCount;
    }

    public void setCashCount(Integer cashCount) {
        this.cashCount = cashCount;
    }

    public BigDecimal getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(BigDecimal cashTotal) {
        this.cashTotal = cashTotal;
    }

    public Integer getBankCount() {
        return bankCount;
    }

    public void setBankCount(Integer bankCount) {
        this.bankCount = bankCount;
    }

    public BigDecimal getBankTotal() {
        return bankTotal;
    }

    public void setBankTotal(BigDecimal bankTotal) {
        this.bankTotal = bankTotal;
    }

    public Integer getChargeCount() {
        return chargeCount;
    }

    public void setChargeCount(Integer chargeCount) {
        this.chargeCount = chargeCount;
    }

    public BigDecimal getChargeTotal() {
        return chargeTotal;
    }

    public void setChargeTotal(BigDecimal chargeTotal) {
        this.chargeTotal = chargeTotal;
    }

    public Integer getRedCount() {
        return redCount;
    }

    public void setRedCount(Integer redCount) {
        this.redCount = redCount;
    }

    public BigDecimal getRedTotal() {
        return redTotal;
    }

    public void setRedTotal(BigDecimal redTotal) {
        this.redTotal = redTotal;
    }

    public Integer getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }

    public BigDecimal getCouponTotal() {
        return couponTotal;
    }

    public void setCouponTotal(BigDecimal couponTotal) {
        this.couponTotal = couponTotal;
    }

    public Integer getWaitCount() {
        return waitCount;
    }

    public void setWaitCount(Integer waitCount) {
        this.waitCount = waitCount;
    }

    public BigDecimal getWaitTotal() {
        return waitTotal;
    }

    public void setWaitTotal(BigDecimal waitTotal) {
        this.waitTotal = waitTotal;
    }

    public Integer getChargeReturnCount() {
        return chargeReturnCount;
    }

    public void setChargeReturnCount(Integer chargeReturnCount) {
        this.chargeReturnCount = chargeReturnCount;
    }

    public BigDecimal getChargeReturnTotal() {
        return chargeReturnTotal;
    }

    public void setChargeReturnTotal(BigDecimal chargeReturnTotal) {
        this.chargeReturnTotal = chargeReturnTotal;
    }

    public Integer getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }

    public BigDecimal getReturnTotal() {
        return returnTotal;
    }

    public void setReturnTotal(BigDecimal returnTotal) {
        this.returnTotal = returnTotal;
    }

    public Integer getOrderItemCount() {
        return orderItemCount;
    }

    public void setOrderItemCount(Integer orderItemCount) {
        this.orderItemCount = orderItemCount;
    }

    public BigDecimal getOrderItemTotal() {
        return orderItemTotal;
    }

    public void setOrderItemTotal(BigDecimal orderItemTotal) {
        this.orderItemTotal = orderItemTotal;
    }

    public BigDecimal getReturnItemTotal() {
        return returnItemTotal;
    }

    public void setReturnItemTotal(BigDecimal returnItemTotal) {
        this.returnItemTotal = returnItemTotal;
    }

    public Integer getReturnItemCount() {
        return returnItemCount;
    }

    public void setReturnItemCount(Integer returnItemCount) {
        this.returnItemCount = returnItemCount;
    }

    public BigDecimal getReturnCustomerTotal() {
        return returnCustomerTotal;
    }

    public void setReturnCustomerTotal(BigDecimal returnCustomerTotal) {
        this.returnCustomerTotal = returnCustomerTotal;
    }

    public Integer getReturnCustomerCount() {
        return returnCustomerCount;
    }

    public void setReturnCustomerCount(Integer returnCustomerCount) {
        this.returnCustomerCount = returnCustomerCount;
    }

    public BigDecimal getNewNormalCustomerTotal() {
        return newNormalCustomerTotal;
    }

    public void setNewNormalCustomerTotal(BigDecimal newNormalCustomerTotal) {
        this.newNormalCustomerTotal = newNormalCustomerTotal;
    }

    public Integer getNewNormalCustomerCount() {
        return newNormalCustomerCount;
    }

    public void setNewNormalCustomerCount(Integer newNormalCustomerCount) {
        this.newNormalCustomerCount = newNormalCustomerCount;
    }

    public BigDecimal getNewCustomerTotal() {
        return newCustomerTotal;
    }

    public void setNewCustomerTotal(BigDecimal newCustomerTotal) {
        this.newCustomerTotal = newCustomerTotal;
    }

    public Integer getNewCustomerCount() {
        return newCustomerCount;
    }

    public void setNewCustomerCount(Integer newCustomerCount) {
        this.newCustomerCount = newCustomerCount;
    }

    public Integer getNewShareCustomerCount() {
        return newShareCustomerCount;
    }

    public void setNewShareCustomerCount(Integer newShareCustomerCount) {
        this.newShareCustomerCount = newShareCustomerCount;
    }

    public BigDecimal getNewShareCustomerTotal() {
        return newShareCustomerTotal;
    }

    public void setNewShareCustomerTotal(BigDecimal newShareCustomerTotal) {
        this.newShareCustomerTotal = newShareCustomerTotal;
    }

    public Integer getBackCustomerCount() {
        return backCustomerCount;
    }

    public void setBackCustomerCount(Integer backCustomerCount) {
        this.backCustomerCount = backCustomerCount;
    }

    public BigDecimal getBackCustomerToatal() {
        return backCustomerToatal;
    }

    public void setBackCustomerToatal(BigDecimal backCustomerToatal) {
        this.backCustomerToatal = backCustomerToatal;
    }

    public Integer getBackTwoCustomerCount() {
        return backTwoCustomerCount;
    }

    public void setBackTwoCustomerCount(Integer backTwoCustomerCount) {
        this.backTwoCustomerCount = backTwoCustomerCount;
    }

    public BigDecimal getBackTwoCustomerTotal() {
        return backTwoCustomerTotal;
    }

    public void setBackTwoCustomerTotal(BigDecimal backTwoCustomerTotal) {
        this.backTwoCustomerTotal = backTwoCustomerTotal;
    }

    public Integer getBackTwoMoreCustomerCount() {
        return backTwoMoreCustomerCount;
    }

    public void setBackTwoMoreCustomerCount(Integer backTwoMoreCustomerCount) {
        this.backTwoMoreCustomerCount = backTwoMoreCustomerCount;
    }

    public BigDecimal getBackTwoMoreCustomerTotal() {
        return backTwoMoreCustomerTotal;
    }

    public void setBackTwoMoreCustomerTotal(BigDecimal backTwoMoreCustomerTotal) {
        this.backTwoMoreCustomerTotal = backTwoMoreCustomerTotal;
    }

    public String getsAvgScore() {
        return sAvgScore;
    }

    public void setsAvgScore(String sAvgScore) {
        this.sAvgScore = sAvgScore == null ? null : sAvgScore.trim();
    }

    public BigDecimal getsRestoTotal() {
        return sRestoTotal;
    }

    public void setsRestoTotal(BigDecimal sRestoTotal) {
        this.sRestoTotal = sRestoTotal;
    }

    public BigDecimal getsEnterTotal() {
        return sEnterTotal;
    }

    public void setsEnterTotal(BigDecimal sEnterTotal) {
        this.sEnterTotal = sEnterTotal;
    }

    public BigDecimal getsAllTotal() {
        return sAllTotal;
    }

    public void setsAllTotal(BigDecimal sAllTotal) {
        this.sAllTotal = sAllTotal;
    }

    public BigDecimal getsTotalIncome() {
        return sTotalIncome;
    }

    public void setsTotalIncome(BigDecimal sTotalIncome) {
        this.sTotalIncome = sTotalIncome;
    }

    public BigDecimal getsPayTotal() {
        return sPayTotal;
    }

    public void setsPayTotal(BigDecimal sPayTotal) {
        this.sPayTotal = sPayTotal;
    }

    public Integer getsNewCustomr() {
        return sNewCustomr;
    }

    public void setsNewCustomr(Integer sNewCustomr) {
        this.sNewCustomr = sNewCustomr;
    }

    public BigDecimal getsNewCustomerTotal() {
        return sNewCustomerTotal;
    }

    public void setsNewCustomerTotal(BigDecimal sNewCustomerTotal) {
        this.sNewCustomerTotal = sNewCustomerTotal;
    }

    public Integer getsNewNormalCustomer() {
        return sNewNormalCustomer;
    }

    public void setsNewNormalCustomer(Integer sNewNormalCustomer) {
        this.sNewNormalCustomer = sNewNormalCustomer;
    }

    public BigDecimal getsNewNormalCustomerTotal() {
        return sNewNormalCustomerTotal;
    }

    public void setsNewNormalCustomerTotal(BigDecimal sNewNormalCustomerTotal) {
        this.sNewNormalCustomerTotal = sNewNormalCustomerTotal;
    }

    public Integer getsNewShareCustomer() {
        return sNewShareCustomer;
    }

    public void setsNewShareCustomer(Integer sNewShareCustomer) {
        this.sNewShareCustomer = sNewShareCustomer;
    }

    public BigDecimal getsNewShareCustomerTotal() {
        return sNewShareCustomerTotal;
    }

    public void setsNewShareCustomerTotal(BigDecimal sNewShareCustomerTotal) {
        this.sNewShareCustomerTotal = sNewShareCustomerTotal;
    }

    public Integer getsBackCustomer() {
        return sBackCustomer;
    }

    public void setsBackCustomer(Integer sBackCustomer) {
        this.sBackCustomer = sBackCustomer;
    }

    public BigDecimal getsBackCustomerTotal() {
        return sBackCustomerTotal;
    }

    public void setsBackCustomerTotal(BigDecimal sBackCustomerTotal) {
        this.sBackCustomerTotal = sBackCustomerTotal;
    }

    public Integer getsBackTwoCustomer() {
        return sBackTwoCustomer;
    }

    public void setsBackTwoCustomer(Integer sBackTwoCustomer) {
        this.sBackTwoCustomer = sBackTwoCustomer;
    }

    public BigDecimal getsBackTwoCustomerTotal() {
        return sBackTwoCustomerTotal;
    }

    public void setsBackTwoCustomerTotal(BigDecimal sBackTwoCustomerTotal) {
        this.sBackTwoCustomerTotal = sBackTwoCustomerTotal;
    }

    public Integer getsBackTwoMoreCustomer() {
        return sBackTwoMoreCustomer;
    }

    public void setsBackTwoMoreCustomer(Integer sBackTwoMoreCustomer) {
        this.sBackTwoMoreCustomer = sBackTwoMoreCustomer;
    }

    public BigDecimal getsBackTwoMoreCustomerTotal() {
        return sBackTwoMoreCustomerTotal;
    }

    public void setsBackTwoMoreCustomerTotal(BigDecimal sBackTwoMoreCustomerTotal) {
        this.sBackTwoMoreCustomerTotal = sBackTwoMoreCustomerTotal;
    }

    public String getzAvgScore() {
        return zAvgScore;
    }

    public void setzAvgScore(String zAvgScore) {
        this.zAvgScore = zAvgScore == null ? null : zAvgScore.trim();
    }

    public BigDecimal getzRestoTotal() {
        return zRestoTotal;
    }

    public void setzRestoTotal(BigDecimal zRestoTotal) {
        this.zRestoTotal = zRestoTotal;
    }

    public BigDecimal getzEnterTotal() {
        return zEnterTotal;
    }

    public void setzEnterTotal(BigDecimal zEnterTotal) {
        this.zEnterTotal = zEnterTotal;
    }

    public BigDecimal getzTotalIncome() {
        return zTotalIncome;
    }

    public void setzTotalIncome(BigDecimal zTotalIncome) {
        this.zTotalIncome = zTotalIncome;
    }

    public BigDecimal getzAllTotal() {
        return zAllTotal;
    }

    public void setzAllTotal(BigDecimal zAllTotal) {
        this.zAllTotal = zAllTotal;
    }

    public BigDecimal getzPayTotal() {
        return zPayTotal;
    }

    public void setzPayTotal(BigDecimal zPayTotal) {
        this.zPayTotal = zPayTotal;
    }

    public BigDecimal getzNewCustomerTotal() {
        return zNewCustomerTotal;
    }

    public void setzNewCustomerTotal(BigDecimal zNewCustomerTotal) {
        this.zNewCustomerTotal = zNewCustomerTotal;
    }

    public Integer getzNewCustomerCount() {
        return zNewCustomerCount;
    }

    public void setzNewCustomerCount(Integer zNewCustomerCount) {
        this.zNewCustomerCount = zNewCustomerCount;
    }

    public Integer getzNewNormalCustomer() {
        return zNewNormalCustomer;
    }

    public void setzNewNormalCustomer(Integer zNewNormalCustomer) {
        this.zNewNormalCustomer = zNewNormalCustomer;
    }

    public BigDecimal getzNewNormalCustomerTotal() {
        return zNewNormalCustomerTotal;
    }

    public void setzNewNormalCustomerTotal(BigDecimal zNewNormalCustomerTotal) {
        this.zNewNormalCustomerTotal = zNewNormalCustomerTotal;
    }

    public Integer getzNewShareCustomer() {
        return zNewShareCustomer;
    }

    public void setzNewShareCustomer(Integer zNewShareCustomer) {
        this.zNewShareCustomer = zNewShareCustomer;
    }

    public BigDecimal getzNewShareCustomerTotal() {
        return zNewShareCustomerTotal;
    }

    public void setzNewShareCustomerTotal(BigDecimal zNewShareCustomerTotal) {
        this.zNewShareCustomerTotal = zNewShareCustomerTotal;
    }

    public Integer getzBackCustomer() {
        return zBackCustomer;
    }

    public void setzBackCustomer(Integer zBackCustomer) {
        this.zBackCustomer = zBackCustomer;
    }

    public BigDecimal getzBackCustomerTotal() {
        return zBackCustomerTotal;
    }

    public void setzBackCustomerTotal(BigDecimal zBackCustomerTotal) {
        this.zBackCustomerTotal = zBackCustomerTotal;
    }

    public Integer getzBackTwoCustomer() {
        return zBackTwoCustomer;
    }

    public void setzBackTwoCustomer(Integer zBackTwoCustomer) {
        this.zBackTwoCustomer = zBackTwoCustomer;
    }

    public BigDecimal getzBackTwoCustomerTotal() {
        return zBackTwoCustomerTotal;
    }

    public void setzBackTwoCustomerTotal(BigDecimal zBackTwoCustomerTotal) {
        this.zBackTwoCustomerTotal = zBackTwoCustomerTotal;
    }

    public Integer getzBackTwoMoreCustomer() {
        return zBackTwoMoreCustomer;
    }

    public void setzBackTwoMoreCustomer(Integer zBackTwoMoreCustomer) {
        this.zBackTwoMoreCustomer = zBackTwoMoreCustomer;
    }

    public BigDecimal getzBackTwoMoreCustomerTotal() {
        return zBackTwoMoreCustomerTotal;
    }

    public void setzBackTwoMoreCustomerTotal(BigDecimal zBackTwoMoreCustomerTotal) {
        this.zBackTwoMoreCustomerTotal = zBackTwoMoreCustomerTotal;
    }

    public String getxAvgScore() {
        return xAvgScore;
    }

    public void setxAvgScore(String xAvgScore) {
        this.xAvgScore = xAvgScore == null ? null : xAvgScore.trim();
    }

    public BigDecimal getxRestoTotal() {
        return xRestoTotal;
    }

    public void setxRestoTotal(BigDecimal xRestoTotal) {
        this.xRestoTotal = xRestoTotal;
    }

    public BigDecimal getxEnterTotal() {
        return xEnterTotal;
    }

    public void setxEnterTotal(BigDecimal xEnterTotal) {
        this.xEnterTotal = xEnterTotal;
    }

    public BigDecimal getxAllTotal() {
        return xAllTotal;
    }

    public void setxAllTotal(BigDecimal xAllTotal) {
        this.xAllTotal = xAllTotal;
    }

    public BigDecimal getxTotalIncome() {
        return xTotalIncome;
    }

    public void setxTotalIncome(BigDecimal xTotalIncome) {
        this.xTotalIncome = xTotalIncome;
    }

    public BigDecimal getxPayTotal() {
        return xPayTotal;
    }

    public void setxPayTotal(BigDecimal xPayTotal) {
        this.xPayTotal = xPayTotal;
    }

    public BigDecimal getxNewCustomerTotal() {
        return xNewCustomerTotal;
    }

    public void setxNewCustomerTotal(BigDecimal xNewCustomerTotal) {
        this.xNewCustomerTotal = xNewCustomerTotal;
    }

    public Integer getxNewNormalCustomer() {
        return xNewNormalCustomer;
    }

    public void setxNewNormalCustomer(Integer xNewNormalCustomer) {
        this.xNewNormalCustomer = xNewNormalCustomer;
    }

    public Integer getxNewCustomer() {
        return xNewCustomer;
    }

    public void setxNewCustomer(Integer xNewCustomer) {
        this.xNewCustomer = xNewCustomer;
    }

    public BigDecimal getxNewNormalCustomerTotal() {
        return xNewNormalCustomerTotal;
    }

    public void setxNewNormalCustomerTotal(BigDecimal xNewNormalCustomerTotal) {
        this.xNewNormalCustomerTotal = xNewNormalCustomerTotal;
    }

    public Integer getxNewShareCustomer() {
        return xNewShareCustomer;
    }

    public void setxNewShareCustomer(Integer xNewShareCustomer) {
        this.xNewShareCustomer = xNewShareCustomer;
    }

    public BigDecimal getxNewShareCustomerTotal() {
        return xNewShareCustomerTotal;
    }

    public void setxNewShareCustomerTotal(BigDecimal xNewShareCustomerTotal) {
        this.xNewShareCustomerTotal = xNewShareCustomerTotal;
    }

    public Integer getxBackCustomer() {
        return xBackCustomer;
    }

    public void setxBackCustomer(Integer xBackCustomer) {
        this.xBackCustomer = xBackCustomer;
    }

    public BigDecimal getxBackCustomerTotal() {
        return xBackCustomerTotal;
    }

    public void setxBackCustomerTotal(BigDecimal xBackCustomerTotal) {
        this.xBackCustomerTotal = xBackCustomerTotal;
    }

    public Integer getxBackTwoCustomer() {
        return xBackTwoCustomer;
    }

    public void setxBackTwoCustomer(Integer xBackTwoCustomer) {
        this.xBackTwoCustomer = xBackTwoCustomer;
    }

    public BigDecimal getxBackTwoCustomerTotal() {
        return xBackTwoCustomerTotal;
    }

    public void setxBackTwoCustomerTotal(BigDecimal xBackTwoCustomerTotal) {
        this.xBackTwoCustomerTotal = xBackTwoCustomerTotal;
    }

    public Integer getxBackTwoMoreCustomer() {
        return xBackTwoMoreCustomer;
    }

    public void setxBackTwoMoreCustomer(Integer xBackTwoMoreCustomer) {
        this.xBackTwoMoreCustomer = xBackTwoMoreCustomer;
    }

    public BigDecimal getxBackTwoMoreCustomerTotal() {
        return xBackTwoMoreCustomerTotal;
    }

    public void setxBackTwoMoreCustomerTotal(BigDecimal xBackTwoMoreCustomerTotal) {
        this.xBackTwoMoreCustomerTotal = xBackTwoMoreCustomerTotal;
    }

    public String getmAvgScore() {
        return mAvgScore;
    }

    public void setmAvgScore(String mAvgScore) {
        this.mAvgScore = mAvgScore == null ? null : mAvgScore.trim();
    }

    public BigDecimal getmRestoTotal() {
        return mRestoTotal;
    }

    public void setmRestoTotal(BigDecimal mRestoTotal) {
        this.mRestoTotal = mRestoTotal;
    }

    public BigDecimal getmEnterTotal() {
        return mEnterTotal;
    }

    public void setmEnterTotal(BigDecimal mEnterTotal) {
        this.mEnterTotal = mEnterTotal;
    }

    public BigDecimal getmAllTotal() {
        return mAllTotal;
    }

    public void setmAllTotal(BigDecimal mAllTotal) {
        this.mAllTotal = mAllTotal;
    }

    public BigDecimal getmTotalIncome() {
        return mTotalIncome;
    }

    public void setmTotalIncome(BigDecimal mTotalIncome) {
        this.mTotalIncome = mTotalIncome;
    }

    public BigDecimal getmPayTotal() {
        return mPayTotal;
    }

    public void setmPayTotal(BigDecimal mPayTotal) {
        this.mPayTotal = mPayTotal;
    }

    public Integer getmNewCustomr() {
        return mNewCustomr;
    }

    public void setmNewCustomr(Integer mNewCustomr) {
        this.mNewCustomr = mNewCustomr;
    }

    public BigDecimal getmNewCustomerTotal() {
        return mNewCustomerTotal;
    }

    public void setmNewCustomerTotal(BigDecimal mNewCustomerTotal) {
        this.mNewCustomerTotal = mNewCustomerTotal;
    }

    public Integer getmNewNormalCustomer() {
        return mNewNormalCustomer;
    }

    public void setmNewNormalCustomer(Integer mNewNormalCustomer) {
        this.mNewNormalCustomer = mNewNormalCustomer;
    }

    public BigDecimal getmNewNormalCustomerTotal() {
        return mNewNormalCustomerTotal;
    }

    public void setmNewNormalCustomerTotal(BigDecimal mNewNormalCustomerTotal) {
        this.mNewNormalCustomerTotal = mNewNormalCustomerTotal;
    }

    public Integer getmNewShareCustomer() {
        return mNewShareCustomer;
    }

    public void setmNewShareCustomer(Integer mNewShareCustomer) {
        this.mNewShareCustomer = mNewShareCustomer;
    }

    public BigDecimal getmNewShareCustomerTotal() {
        return mNewShareCustomerTotal;
    }

    public void setmNewShareCustomerTotal(BigDecimal mNewShareCustomerTotal) {
        this.mNewShareCustomerTotal = mNewShareCustomerTotal;
    }

    public Integer getmBackCustomer() {
        return mBackCustomer;
    }

    public void setmBackCustomer(Integer mBackCustomer) {
        this.mBackCustomer = mBackCustomer;
    }

    public BigDecimal getmBackCustomerTotal() {
        return mBackCustomerTotal;
    }

    public void setmBackCustomerTotal(BigDecimal mBackCustomerTotal) {
        this.mBackCustomerTotal = mBackCustomerTotal;
    }

    public Integer getmBackTwoCustomer() {
        return mBackTwoCustomer;
    }

    public void setmBackTwoCustomer(Integer mBackTwoCustomer) {
        this.mBackTwoCustomer = mBackTwoCustomer;
    }

    public BigDecimal getmBackTwoCustomerTotal() {
        return mBackTwoCustomerTotal;
    }

    public void setmBackTwoCustomerTotal(BigDecimal mBackTwoCustomerTotal) {
        this.mBackTwoCustomerTotal = mBackTwoCustomerTotal;
    }

    public Integer getmBackTwoMoreCustomer() {
        return mBackTwoMoreCustomer;
    }

    public void setmBackTwoMoreCustomer(Integer mBackTwoMoreCustomer) {
        this.mBackTwoMoreCustomer = mBackTwoMoreCustomer;
    }

    public BigDecimal getmBackTwoMoreCustomerTotal() {
        return mBackTwoMoreCustomerTotal;
    }

    public void setmBackTwoMoreCustomerTotal(BigDecimal mBackTwoMoreCustomerTotal) {
        this.mBackTwoMoreCustomerTotal = mBackTwoMoreCustomerTotal;
    }
}