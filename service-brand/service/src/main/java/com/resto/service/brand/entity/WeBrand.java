package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WeBrand implements Serializable {

    private static final long serialVersionUID = 8220760285480417272L;

    private Long id;

    private String brandId;

    private String brandName;

    List<WeBrandScore> weBrandScores;

}