package com.record.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxModel {

	//private Long id;
	private String taxCode;
	private String taxName;
	private String regNo;
	private Long rate;
	private String calculationCode;
	private Long tdsWthTax;
	private Boolean isTdsWthReq;
	private Boolean isTaxUnderRevCharge;
	private Boolean isActive;
	private Boolean isNegative;
	private String businessEntityId;
	
	
}
