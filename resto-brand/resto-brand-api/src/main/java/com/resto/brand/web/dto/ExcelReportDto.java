package com.resto.brand.web.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReportDto implements Serializable {
	
	//定义数据集合
	private List<?> result;
	
	//定义map
	Map<String,String> map = new HashMap<>() ;
	
	//定义列
	String[]columns;
	//定义表头
	String[][] headers;
	
	public List<?> getResult() {
		return result;
	}
	public void setResult(List<?> result) {
		this.result = result;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public String[] getColumns() {
		return columns;
	}
	public void setColumns(String[] columns) {
		this.columns = columns;
	}
	public String[][] getHeaders() {
		return headers;
	}
	public void setHeaders(String[][] headers) {
		this.headers = headers;
	}
	
}
