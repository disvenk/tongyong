package com.resto.api.shop.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class AdvertDto implements Serializable {

    private static final long serialVersionUID = -8028917166703164050L;

    private Integer id;
    
    @NotBlank(message="{标题不能为空}")
    private String slogan;

    @NotBlank(message="{描述不能为空}")
    private String description;

    private Byte state;

    private String shopDetailId;

}