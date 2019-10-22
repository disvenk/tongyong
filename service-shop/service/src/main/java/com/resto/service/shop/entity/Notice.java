package com.resto.service.shop.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class Notice implements Serializable {

    private static final long serialVersionUID = 8157121811715415686L;

    private String id;
    
    @NotBlank(message="通知标题不能为空")
    private String title;

    private String content;

    private Date createDate;
    
    @NotNull(message="排序不能为空")
    private Integer sort;

    private Byte status;

    @NotBlank(message="请上传图片！")
    private String noticeImage;

    @NotNull(message="请选择通知类型！")
    private Byte noticeType;

    private String shopDetailId;
    
    private Boolean isRead;

    //弹窗显示次数
    private Integer showTimes;

    //显示时间段
    private Integer[] supportTimes;

}