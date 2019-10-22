package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class Notice implements Serializable {
    private String id;
    
    @NotBlank(message="通知标题不能为空")
    private String title;

    private String content;

    private Date createDate;
    
    @NotNull(message="排序不能为空")
    private Integer sort;

    private Byte status;

    @NotBlank(message="请上传图片！")
    private String noticeImage;

    @NotNull(message="请选择通知类型！")
    private Byte noticeType;

    private String shopDetailId;
    
    private Boolean isRead;

    //弹窗显示次数
    private Integer showTimes;

    //显示时间段
    private Integer[] supportTimes;

    final public Integer[] getSupportTimes() {
        return supportTimes;
    }

    final public void setSupportTimes(Integer[] supportTimes) {
        this.supportTimes = supportTimes;
    }

    final public Integer getShowTimes() {
        return showTimes;
    }

    final public void setShowTimes(Integer showTimes) {
        this.showTimes = showTimes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getNoticeImage() {
        return noticeImage;
    }

    public void setNoticeImage(String noticeImage) {
        this.noticeImage = noticeImage == null ? null : noticeImage.trim();
    }

    public Byte getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Byte noticeType) {
        this.noticeType = noticeType;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
}