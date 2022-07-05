package com.record.model;

import java.util.Date;
import java.util.Objects;

public enum BusinessEntityEnum {

	ID("ID","id"),
	ENTITYID("ENTITYID","entityId"),
	LEGALENTITYNAME("LEGALENTITYNAME","entityLegalName"),
	ENTITYSHORTNAME("ENTITYSHORTNAME","entityShortName"),
	HOMECOUNTRY("HOMECOUNTRY","homeCountry"),
	STATUS("STATUS","status"),
	BASECURRENCY("BASECURRENCY","baseCurrency"),
	ENTITYHOSTNAME("ENTITYHOSTNAME","entityHostName"),
	ENTITYHOSTPORT("ENTITYHOSTPORT","entityHostPort"),
	ENTITYADDRESS("ENTITYADDRESS","entityAddress"),
	ENTITYTYPE("ENTITYTYPE","entityType"),
	PINCODE("PINCODE","pinCode"),
	STATECODE("STATECODE","stateCode"),
	RATINGAGENCY("RATINGAGENCY","ratingAgency"),
	RATING("RATING","rating"),
	INCOMETAX("INCOMETAX","incomeTaxIdentificationNumber"),
	TDSWITRATE("TDSWITRATE","tdsWhtRateApplicable");
	
	public String getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
	private final String key; 
	private final String value;
	
	private BusinessEntityEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public static BusinessEntityEnum valueFrom(String key) {
		BusinessEntityEnum value = null;
		if (Objects.nonNull(key)) {
			try {
				value = BusinessEntityEnum.valueOf(key);
			} catch (Exception ex) {
				// Not Throwing any exception rather returning Null value back.
			}
		}
		return value;
	}
	
	
	
}
