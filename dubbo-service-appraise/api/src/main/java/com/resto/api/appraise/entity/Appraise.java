package com.resto.api.appraise.entity;

import com.resto.conf.db.BaseEntityResto;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "tb_appraise")
public class Appraise extends BaseEntityResto implements Serializable {

    private static final long serialVersionUID = -1880834688424235861L;

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

    @Transient
    private String brandId;
    
    /**
     * 评论者昵称
     */
    @Transient
    private String nickName;
    
    /**
     * 评论者照片
     */
    @Transient
    private String headPhoto;

    /**
     * 菜品名称
     */
    @Transient
    private String articleName;
    
    /**
     * 是否能打赏
     */
    @Transient
    private Boolean canReward;

    /**
     * 点赞
     */
    @Transient
    private List<AppraisePraise> appraisePraises;

    /**
     *  评论
     */
    @Transient
    private List<AppraiseComment> appraiseComments;

    /**
     * 照片
     */
    @Transient
    private List<AppraiseFile> appraiseFiles;

    /**
     * 消息体验
     */
    @Transient
    private List<AppraiseFile> appraiseMessage;

    @Transient
    private String shopName;

    @Transient
    private String sex;

    //反馈选择的餐品
    private String feedbackArticle;

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