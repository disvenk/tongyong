package com.resto.api.shop.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class ArticleFamilyDto implements Serializable{

    private static final long serialVersionUID = 1775921374154136921L;

    private String id;

    @NotBlank(message="{类型名称   不能为空}")
    private String name;

    @NotNull(message="{序号   不能为空}")
    private Integer peference;

    private String parentId;

    private Integer level;

    private String shopDetailId;
    
    @NotNull(message="{就餐模式   不能为空}")
    private Integer distributionModeId;

    private List<ArticleDto> articleList;

}