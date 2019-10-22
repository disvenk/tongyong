package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信模板消息
 * @author Administrator
 *
 */
@Data
public class TemplateMessage implements Serializable {

	private static final long serialVersionUID = 4039983941171030103L;

	private Map<String,TemplateItem> data = new HashMap<String,TemplateItem>();
	private String touser; //微信id
	private String template_id; //消息模板
	private String url;		//消息url

	public void add(String key,String value,String color){
		data.put(key, new TemplateItem(value, color));
	}

	public void setData(Map<String, TemplateItem> data) {
		this.data = data;
	}

	public void add(String key, String value) {
		this.add(key, value, "#000000");
	}
	
}
