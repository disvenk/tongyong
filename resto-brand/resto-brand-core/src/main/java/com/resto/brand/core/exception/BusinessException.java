package com.resto.brand.core.exception;

/**
*@Description:自定义业务异常
*@Author:disvenk.dai
*@Date:16:17 2018/9/19 0019
*/
public class BusinessException extends RuntimeException {

	/**
	 * serializable
	 */
	private static final long serialVersionUID = 1L;

	private ExceptionType exceptionType;

	public BusinessException(final ExceptionType exceptionType) {
		super(exceptionType.toString());
		this.exceptionType = exceptionType;
	}

	public ExceptionType getExceptionType() {

		return exceptionType;
	}

	public void setExceptionType(final ExceptionType exceptionType) {

		this.exceptionType = exceptionType;
	}

	public static long getSerialversionuid() {

		return serialVersionUID;
	}

	/**
	 * 获取异常码
	 * 
	 * @return
	 */
	public Integer getExceptionCode() {

		return this.exceptionType.getExceptionCode();
	}

	/**
	 * 获取异常描述
	 * 
	 * @return
	 */
	public String getExceptionMsg() {

		return this.exceptionType.getExceptionMsg();
	}

}
