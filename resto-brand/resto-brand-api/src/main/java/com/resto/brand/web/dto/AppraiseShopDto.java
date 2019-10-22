package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 店铺评论  （用于报表）
 *
 */
public class AppraiseShopDto implements Serializable {
	
	private String levelName;//评论等级

    private Integer level;
	
	private String telephone;//手机号
	
	private BigDecimal orderMoney;//订单金额
	
	private BigDecimal redMoney;//评论金额
	
	private String feedBack;//评论对象
	
	private String content;//评论内容

	private String createTime;//评论时间

	private Integer tablenumber;//桌号

	private String areaname;//区域

    private List<Map<String, Object>> appraiseShopDtos;

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public Integer getTablenumber() {
		return tablenumber;
	}

	public void setTablenumber(Integer tablenumber) {
		this.tablenumber = tablenumber;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getRedMoney() {
		return redMoney;
	}

	public void setRedMoney(BigDecimal redMoney) {
		this.redMoney = redMoney;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<Map<String, Object>> getAppraiseShopDtos() {
        return appraiseShopDtos;
    }

    public void setAppraiseShopDtos(List<Map<String, Object>> appraiseShopDtos) {
        this.appraiseShopDtos = appraiseShopDtos;
    }
}
