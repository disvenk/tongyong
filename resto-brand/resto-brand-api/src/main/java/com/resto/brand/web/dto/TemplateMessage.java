package com.resto.brand.web.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信模板消息
 * @author Administrator
 *
 */
public class TemplateMessage implements Serializable {
	private Map<String,TemplateItem> data = new HashMap<String,TemplateItem>();
	private String touser; //微信id
	private String template_id; //消息模板
	private String url;		//消息url
	
	public TemplateMessage() {
	}
	
	public TemplateMessage(String tempId) {
		this.template_id = tempId;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void add(String key,String value,String color){
		data.put(key, new TemplateItem(value, color));
	}
	
	public Map<String, TemplateItem> getData() {
		return data;
	}

	public void setData(Map<String, TemplateItem> data) {
		this.data = data;
	}

	public void add(String key, String value) {
		this.add(key, value, "#000000");
	}
	
}
