package com.resto.api.appraise.entity;

import com.resto.conf.db.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "tb_appraise_new")
public class AppraiseNew extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 740584833130261685L;

    private String content;

    private String customerId;

    private String orderId;

    @Transient
    private String brandId;

    private String shopDetailId;

    private BigDecimal allGrade;

    private BigDecimal redMoney;

    private Integer level;

    private String feedback;

    private Date createTime;

    private Date updateTime;

    @Transient
    private List<AppraiseGrade> appraiseGrades;//评分类型(服务、环境、性价比、氛围)

    @Transient
    private List<AppraiseStep> appraiseSteps;//菜品赞踩

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
     * 评论者性别
     */
    @Transient
    private String sex;

}