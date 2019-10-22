package com.resto.shop.web.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class Menu implements Serializable{

    private static final long serialVersionUID = -28562881406580593L;

    private Long id;

    private Byte menuType;

    private String menuVersion;

    private String menuName;

    private Boolean menuState;

    private String createTime;

    private String updateTime;

    private String createUser;

    private String updateUser;

    private Byte menuCycle;

    private String startTime;

    private String endTime;

    private String remark;

    private Integer parentId;
}