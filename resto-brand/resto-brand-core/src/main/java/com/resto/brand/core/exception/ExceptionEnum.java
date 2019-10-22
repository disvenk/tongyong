package com.resto.brand.core.exception;

import org.apache.commons.lang3.Validate;

import java.text.MessageFormat;

/**
*@Description:异常类型枚举
*@Author:disvenk.dai
*@Date:16:17 2018/9/19 0019
*/
public enum ExceptionEnum {

	EXECUTE_RUNTIME_EXCP(new ExceptionType(1001, "系统异常")),// 基础系统异常
	SERVER_BUSZY(new ExceptionType(1002,"服务器繁忙，请稍后再试")),
	NETWORK_BAD(new ExceptionType(1003,"系统检测到您的网络出现故障，请稍后重试")),
	SERVER_MANTAINANCE(new ExceptionType(1004,"服务器维护中,如点餐业务异常,请稍后再试"));

	private ExceptionType exceptionType;

	ExceptionEnum(final ExceptionType exceptionType) {

		this.exceptionType = exceptionType;
	}

	/**
	*@Description:设置exceptionMsg，返回一个新的ExceptionType实例
	*@Author:disvenk.dai
	*@Date:16:18 2018/9/19 0019
	*/
	public ExceptionType setExceptionMsg(final String exceptionMsg) {

		return new ExceptionType(exceptionType.getExceptionCode(), exceptionMsg);
	}


	/**
	*@Description:设置格式化模板的exceptionMsg<br>,调用{0}系统的{1}接口失败
	*@Author:disvenk.dai
	*@Date:16:18 2018/9/19 0019
	*/
	public ExceptionType setFormatMsg(final String exceptionMsg,
			final Object... obj) {

		Validate.noNullElements(obj);
		final String msg = MessageFormat.format(exceptionMsg, obj);
		return new ExceptionType(exceptionType.getExceptionCode(), msg);
	}

	public ExceptionType getExceptionType() {

		return exceptionType;
	}

	public void setExceptionType(final ExceptionType exceptionType) {

		this.exceptionType = exceptionType;
	}

}
