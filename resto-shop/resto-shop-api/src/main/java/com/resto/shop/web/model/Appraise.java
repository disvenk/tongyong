package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class Appraise implements Serializable {
    private String id;

    private String pictureUrl;

    private Byte level;

    private Date createTime;

    private String content;

    private Byte status;

    private Byte type;

    private String feedback;

    private BigDecimal redMoney;

    private String customerId;

    private String orderId;

    private String articleId;

    private String shopDetailId;

    private String brandId;
    
    /**
     * 评论者昵称
     */
    private String nickName;
    
    /**
     * 评论者照片
     */
    private String headPhoto;

    /**
     * 菜品名称
     */
    private String articleName;
    
    /**
     * 是否能打赏
     */
    private Boolean canReward;

    /**
     * 点赞
     */
    private List<AppraisePraise> appraisePraises;

    /**
     *  评论
     */
    private List<AppraiseComment> appraiseComments;

    /**
     * 照片
     */
    private List<AppraiseFile> appraiseFiles;

    /**
     * 消息体验
     */
    private List<AppraiseFile> appraiseMessage;

    private String shopName;

    private String sex;

    //反馈选择的餐品
    private String feedbackArticle;

    public String getFeedbackArticle() {
        return feedbackArticle;
    }

    public void setFeedbackArticle(String feedbackArticle) {
        this.feedbackArticle = feedbackArticle;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<AppraiseFile> getAppraiseFiles() {
        return appraiseFiles;
    }

    public void setAppraiseFiles(List<AppraiseFile> appraiseFiles) {
        this.appraiseFiles = appraiseFiles;
    }

    public List<AppraisePraise> getAppraisePraises() {
        return appraisePraises;
    }

    public void setAppraisePraises(List<AppraisePraise> appraisePraises) {
        this.appraisePraises = appraisePraises;
    }

    public List<AppraiseComment> getAppraiseComments() {
        return appraiseComments;
    }

    public void setAppraiseComments(List<AppraiseComment> appraiseComments) {
        this.appraiseComments = appraiseComments;
    }

    final public String getBrandId() {
        return brandId;
    }

    final public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl == null ? null : pictureUrl.trim();
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback == null ? null : feedback.trim();
    }

    public BigDecimal getRedMoney() {
        return redMoney;
    }

    public void setRedMoney(BigDecimal redMoney) {
        this.redMoney = redMoney;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public Boolean getCanReward() {
		return canReward;
	}

	public void setCanReward(Boolean canReward) {
		this.canReward = canReward;
	}

    public List<AppraiseFile> getAppraiseMessage() {
        return appraiseMessage;
    }

    public void setAppraiseMessage(List<AppraiseFile> appraiseMessage) {
        this.appraiseMessage = appraiseMessage;
    }

    public static final String getLevel(Byte value){
        switch (value){
            case 1:
                return "一星";
            case 2:
                return "二星";
            case 3:
                return "三星";
            case 4:
                return "五星";
            case 5:
                return "五星";
            default:
                return "未知评价";
        }
    }
}