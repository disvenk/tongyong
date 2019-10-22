package com.resto.api.appraise.entity;

import com.resto.conf.db.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "tb_appraise_step")
public class AppraiseStep extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1655964273335410428L;

    private Long appraiseId;

    private String articleId;

    private Integer state;

    @Transient
    private String pictureUrl;

    private Date createTime;

    private Date updateTime;
}