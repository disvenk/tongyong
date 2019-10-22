package com.resto.brand.core.entity;

import java.io.Serializable;

/**
 * 数据验证异常
 * @author Diamond
 * @date 2016年3月15日
 */
public class DataVailedException extends RuntimeException implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4625034797028307097L;

	private String message;
	
	public DataVailedException(String string) {
		this.message = string;
	}
	@Override
	public String getMessage() {
		return message;
	}

}
