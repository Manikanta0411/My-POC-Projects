package com.record.dto;

import java.util.Collection;
import java.util.List;

public class ParamFieldDto {
    private Long id;
    private String originId;
    private String fieldKey;
    private String fieldValue;
    private List<PpwDto> ppwDto;
    private String fieldType;
    private String dataType;
    private List<String> entityIds;
    private Collection<Long> businessEntityIds;
    private List<BusinessEntityDto> businessEntityDtos;
    private String fieldCode;
    private String oprationType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }


	public List<PpwDto> getPpwDto() {
		return ppwDto;
	}

	public void setPpwDto(List<PpwDto> ppwDto) {
		this.ppwDto = ppwDto;
	}

	public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public List<String> getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(List<String> entityIds) {
        this.entityIds = entityIds;
    }

    public Collection<Long> getBusinessEntityIds() {
        return businessEntityIds;
    }

    public void setBusinessEntityIds(Collection<Long> businessEntityIds) {
        this.businessEntityIds = businessEntityIds;
    }

    public List<BusinessEntityDto> getBusinessEntityDtos() {
        return businessEntityDtos;
    }

    public void setBusinessEntityDtos(List<BusinessEntityDto> businessEntityDtos) {
        this.businessEntityDtos = businessEntityDtos;
    }

	public String getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getOprationType() {
		return oprationType;
	}

	public void setOprationType(String oprationType) {
		this.oprationType = oprationType;
	}
    
    
}
