package com.resto.service.brand.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class DistributionMode implements Serializable {

    private static final long serialVersionUID = -9219550960724461039L;

    @NotNull(message="配送模式不能为空")
    private Integer id;

    @NotBlank(message="配送名称不能为空")
    private String name;

    private String remark;

}