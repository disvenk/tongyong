package com.resto.api.appraise.entity;

import com.resto.conf.db.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "tb_appraise_grade")
public class AppraiseGrade extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1420569054668985764L;

    private Long appraiseId;

    private Integer type;

    private BigDecimal grade;

    private Date createTime;

    private Date updateTime;


}