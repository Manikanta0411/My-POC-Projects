package com.record.dto;

import java.math.BigDecimal;
import java.util.List;

public class LayerDto {

    private Long id;
    private String originId;
    private String name;
    private List<ParamFieldDto> paramFieldDtos;
    private BigDecimal premiumRate;
    private String premiumRateField;
    private String remarks;
    private List<Long> entitiesId;
    

    public List<Long> getEntitiesId() {
		return entitiesId;
	}

	public void setEntitiesId(List<Long> entitiesId) {
		this.entitiesId = entitiesId;
	}

	public Long getId() {
        return id;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParamFieldDto> getParamFieldDtos() {
        return paramFieldDtos;
    }

    public void setParamFieldDtos(List<ParamFieldDto> paramFieldDtos) {
        this.paramFieldDtos = paramFieldDtos;
    }

    public BigDecimal getPremiumRate() {
        return premiumRate;
    }

    public void setPremiumRate(BigDecimal premiumRate) {
        this.premiumRate = premiumRate;
    }

    public String getPremiumRateField() {
        return premiumRateField;
    }

    public void setPremiumRateField(String premiumRateField) {
        this.premiumRateField = premiumRateField;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
