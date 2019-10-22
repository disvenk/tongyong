package com.resto.api.appraise.dto;

import com.resto.api.appraise.entity.AppraiseComment;
import com.resto.api.appraise.entity.AppraiseGrade;
import com.resto.api.appraise.entity.AppraisePraise;
import com.resto.api.appraise.entity.AppraiseStep;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author lhf
 * @create 2018/04/13 11:06
 * @desc
 **/
@Data
public class NewAppraiseCustomerDto implements Serializable{

    private static final long serialVersionUID = -5050545903462633688L;

    private Long appraiseId;

    private String headPhoto;

    private String nickName;

    private String content;

    private BigDecimal allGrade;

    private String feedback;

    private Integer level;

    private String createTime;

    private List<AppraisePraise> appraisePraises;

    private List<AppraiseComment> appraiseComments;

    private List<AppraiseGrade> appraiseGrades;//评分类型(服务、环境、性价比、氛围)

    private List<AppraiseStep> appraiseSteps;//菜品赞踩

}
