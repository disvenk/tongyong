package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class AppraiseDto implements Serializable {

    private static final long serialVersionUID = -1880834688424235861L;

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
    private List<AppraisePraiseDto> appraisePraises;

    /**
     *  评论
     */
    private List<AppraiseCommentDto> appraiseComments;

    /**
     * 照片
     */
    private List<AppraiseFileDto> appraiseFiles;

    /**
     * 消息体验
     */
    private List<AppraiseFileDto> appraiseMessage;

    private String shopName;

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