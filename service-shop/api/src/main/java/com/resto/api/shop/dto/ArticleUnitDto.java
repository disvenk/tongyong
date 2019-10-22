package com.resto.api.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleUnitDto implements Serializable{

    private static final long serialVersionUID = -1250325649049585511L;

    private Integer id;

    private String name;

    private BigDecimal sort;

    private Integer tbArticleAttrId;

    private Integer count;

    private List<ArticlePriceDto> articlePrices;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        if (count == 0){
            this.count = 0;
        }
        this.count = count;
    }

}