package com.resto.api.brand.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ExcelReportDto {
	
	//定义数据集合
	private List<?> result;

	//定义map
	Map<String,String> map = new HashMap<>() ;

	//定义列
	String[]columns;

	//定义表头
	String[][] headers;
	
}
