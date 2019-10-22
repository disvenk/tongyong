package com.resto.brand.web.model;

import java.io.Serializable;

/**
 * Created by KONATA on 2016/11/1.
 * 第三方平台
 */
public class Platform implements Serializable {

    private Long id;

    private String name;
    
    private String platformId;

    final public Long getId() {
        return id;
    }

    final public void setId(Long id) {
        this.id = id;
    }

    final public String getName() {
        return name;
    }

    final public void setName(String name) {
        this.name = name;
    }

    final public String getPlatformId() {
		return platformId;
	}

    final public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
    
    
}
