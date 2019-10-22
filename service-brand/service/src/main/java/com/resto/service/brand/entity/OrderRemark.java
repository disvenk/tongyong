package com.resto.service.brand.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRemark implements Serializable {
    private static final long serialVersionUID = -7410660872718094551L;
    private String id;
    private String remarkName;
    private Integer sort;
    private Integer state;
    private Date createTime;

}
