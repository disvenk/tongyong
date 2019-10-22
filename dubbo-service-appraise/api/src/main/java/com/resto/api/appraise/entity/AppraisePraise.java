package com.resto.api.appraise.entity;

import com.resto.conf.db.BaseEntityResto;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "tb_appraise_praise")
public class AppraisePraise extends BaseEntityResto implements Serializable {

    private static final long serialVersionUID = -799973958217800162L;

    private String appraiseId;

    private Date createTime;

    private String customerId;

    private Integer isDel;

    private String shopDetailId;

    private String brandId;

    /**
     * 评论者照片
     */
    @Transient
    private String headPhoto;

}
