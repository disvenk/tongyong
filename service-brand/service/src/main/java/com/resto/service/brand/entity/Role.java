package com.resto.service.brand.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class Role implements Serializable {

    private static final long serialVersionUID = -7203857674624446588L;

    private Long id;

    private String description;

    @NotBlank(message="角色名不得为空")
    private String roleName;

    @NotBlank(message="角色标示不得为空")
    private String roleSign;
    
    private Long userGroupId;

}