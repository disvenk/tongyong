package com.resto.api.appraise.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AppraisePraiseDto implements Serializable {

    private static final long serialVersionUID = -799973958217800162L;

    private String id;

    private String appraiseId;

    private Date createTime;

    private String customerId;

    private Integer isDel;

    private String shopDetailId;

    private String brandId;

    /**
     * 评论者照片
     */
    private String headPhoto;

}
