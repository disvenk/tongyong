package com.resto.brand.core.exception;

/**
*@Description:异常类型定义
*@Author:disvenk.dai
*@Date:16:17 2018/9/19 0019
*/
public class ExceptionType {

	/**
	 * 异常码
	 */
	private Integer exceptionCode;

	/**
	 * 异常描述
	 */
	private String exceptionMsg;

	public ExceptionType(final Integer errorCode, final String errorMsg) {

		this.exceptionCode = errorCode;
		this.exceptionMsg = errorMsg;
	}

	public ExceptionType(final Integer errorCode) {

		this.exceptionCode = errorCode;
	}

	public Integer getExceptionCode() {

		return exceptionCode;
	}

	public void setExceptionCode(final Integer exceptionCode) {

		this.exceptionCode = exceptionCode;
	}

	public String getExceptionMsg() {

		return exceptionMsg;
	}

	public void setExceptionMsg(final String exceptionMsg) {

		this.exceptionMsg = exceptionMsg;
	}

	@Override
	public String toString() {
		return "{" +
				"exceptionCode:'" + exceptionCode + '\'' +
				", exceptionMsg:'" + exceptionMsg + '\'' +
				'}';
	}
}
