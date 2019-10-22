package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TemplateItem implements Serializable {

	private static final long serialVersionUID = -7930228166006191366L;

	private String value;

	private String color;
	
	public TemplateItem() {
	}
	
	public TemplateItem(String value,String color){
		setValue(value);
		setColor(color);
	}

}
