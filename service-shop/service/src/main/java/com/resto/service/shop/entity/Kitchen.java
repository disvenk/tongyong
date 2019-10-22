package com.resto.service.shop.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class Kitchen implements Serializable {

    private static final long serialVersionUID = -8136607306814729763L;

    private Integer id;

    @NotBlank(message="{厨房名称   不能为空}")
    private String name;

    private String remark;

    private Integer printerId;

    private String shopDetailId;

    //关联查询 打印机的名称
    private String printerName;

}