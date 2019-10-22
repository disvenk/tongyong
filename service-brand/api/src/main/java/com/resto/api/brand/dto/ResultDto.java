package com.resto.api.brand.dto;

import com.resto.api.brand.util.BrandConstant;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ResultDto implements Serializable {

    private static final long serialVersionUID = 6288374846131788743L;

    @ApiModelProperty(value = "返回信息")
    private String message;

    @ApiModelProperty(value = "状态码")
    private int statusCode;

    @ApiModelProperty(value = "返回数据")
    private Object data;

    public ResultDto() {
    }

    public static ResultDto getSuccess() {
        return new ResultDto(BrandConstant.MESSAGE_OK, BrandConstant.SUCCESS_CODE);
    }

    public static ResultDto getSuccess(String message) {
        return new ResultDto(message, BrandConstant.SUCCESS_CODE, BrandConstant.TRUE);
    }

    public static ResultDto getSuccess(Object data) {
        return new ResultDto(BrandConstant.MESSAGE_OK, BrandConstant.SUCCESS_CODE, data);
    }

    public static ResultDto getError(String message) {
        return new ResultDto(message, BrandConstant.ERROR_CODE, BrandConstant.FALSE);
    }

    public ResultDto(String message, int statusCode, Object data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }

    public ResultDto(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
