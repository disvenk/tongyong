package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收入统计  （用于报表）
 * @author lmx
 *
 */
@Data
public class IncomeReportDto implements Serializable {

	private static final long serialVersionUID = 9156173905161178481L;
	
	private BigDecimal payValue;

	private Integer payMentModeId;

	private String shopDetailId;

	private String brandId;

}
