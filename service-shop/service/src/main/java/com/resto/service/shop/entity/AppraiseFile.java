package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AppraiseFile implements Serializable {

    private static final long serialVersionUID = -8561276482260697283L;

    private String id;

    private String appraiseId;

    private Date createTime;

    private String fileUrl;

    private String shopDetailId;

    private Integer sort;

    private String photoSquare;

    private String fileName;

    private Integer state;

}
