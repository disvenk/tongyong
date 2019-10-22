package com.resto.brand.web.dto;

import com.resto.brand.core.feature.orm.mybatis.Page;

import java.io.Serializable;
import java.util.List;

public class DataTablePageDto<T> implements Serializable {
	private List<T> data;		 //包含的数据
	private Integer draw=1;        //绘制次数
	private Long recordsTotal;//总长度
	private Long recordsFiltered;//过滤后的长度
	private Integer start=0;    //页面开始位置
	private Integer length=10;  //页面结束位置
	private String keyword;     //搜索关键字
	
	public DataTablePageDto() {
	}
	public DataTablePageDto(Page<T> page){
		this.data = page.getResult();
		this.draw = page.getPageNo();
		this.recordsTotal = (long) page.getTotalCount();
		this.recordsFiltered=this.recordsTotal;
	}
	
	public DataTablePageDto(Integer start, Integer length) {
		this.start = start;
		this.length = length;
	}

	public List<T> getData() {
		return data;
	}
	public void setData(List<T> list) {
		this.data = list;
	}
	public Integer getDraw() {
		return draw;
	}
	public void setDraw(Integer draw) {
		this.draw = draw;
	}
	public Long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public Long getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(Long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
}
