package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PlatformDto implements Serializable{

    private static final long serialVersionUID = 1243798583617032128L;

    private Long id;

    private String name;

    private String platformId;

}
