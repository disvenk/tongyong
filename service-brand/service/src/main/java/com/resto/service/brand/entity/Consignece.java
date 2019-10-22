package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 收货人信息
 * @author Administrator
 *
 */
@Data
public class Consignece implements Serializable {

	private static final long serialVersionUID = -5893616600835286219L;

	private String id;

	private String brandId;

	private String name;

	private String phone;

	private String address;

}
