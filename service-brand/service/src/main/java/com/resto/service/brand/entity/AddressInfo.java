package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 收货
 * @author Administrator
 *
 */
@Data
public class AddressInfo implements Serializable {

	private static final long serialVersionUID = 4055411646985676750L;

	private String id;

	private String brandId;

	private String name;

	private String phone;

	private String address;

	private Integer state;

}
