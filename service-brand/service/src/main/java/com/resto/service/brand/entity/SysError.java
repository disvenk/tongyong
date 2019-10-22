package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by KONATA on 2016/11/18.
 * 系统异常
 */
@Data
public class SysError implements Serializable {

    private static final long serialVersionUID = 1704792833560785503L;
    //主键
    private Long id;
    //店铺id
    private String shopDetailId;
    //错误日志
    private String errorMsg;
    //品牌id
    private String brandId;
    //产生时间
    private Date createTime;
    //是否修复
    private Integer isFixed;
    //请求url
    private String apiUrl;
    //错误类型
    private String errorType;

}
