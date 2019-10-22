package com.resto.brand.web.dto;

import java.io.Serializable;

public class TemplateItem implements Serializable {
	private String value;
	private String color;
	
	public TemplateItem() {
	}
	
	public TemplateItem(String value,String color){
		setValue(value);
		setColor(color);
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
