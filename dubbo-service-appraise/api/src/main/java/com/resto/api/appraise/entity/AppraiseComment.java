package com.resto.api.appraise.entity;

import com.resto.conf.db.BaseEntityResto;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "tb_appraise_comment")
public class AppraiseComment extends BaseEntityResto implements Serializable {

    private static final long serialVersionUID = 4864271636777136512L;

    private String appraiseId;

    private Date createTime;

    private String customerId;

    private Integer isDel;

    private String shopDetailId;

    private String brandId;

    private String pid;

    private String content;

    @Transient
    private String nickName;

    /**
     * 回复的对象昵称
     */
    @Transient
    private String replyName;

}
