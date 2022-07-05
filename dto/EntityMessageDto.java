package com.record.dto;

import java.util.List;

public class EntityMessageDto {
    private Long entityId;
    private String senderEntityId;
    private Long layerId;
    private Long riskId;
    private RiskDto riskDto;
    private LayerDto layerDto;
    private List<ParamFieldDto> paramFields;
    private List<PpwDto> ppwDto;
    private List<Long> attachmentIds;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getSenderEntityId() {
        return senderEntityId;
    }

    public void setSenderEntityId(String senderEntityId) {
        this.senderEntityId = senderEntityId;
    }

    public Long getLayerId() {
        return layerId;
    }

    public void setLayerId(Long layerId) {
        this.layerId = layerId;
    }

    public RiskDto getRiskDto() {
        return riskDto;
    }

    public void setRiskDto(RiskDto riskDto) {
        this.riskDto = riskDto;
    }

    public LayerDto getLayerDto() {
        return layerDto;
    }

    public void setLayerDto(LayerDto layerDto) {
        this.layerDto = layerDto;
    }

    public Long getRiskId() {
        return riskId;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }


    public List<PpwDto> getPpwDto() {
		return ppwDto;
	}

	public void setPpwDto(List<PpwDto> ppwDto) {
		this.ppwDto = ppwDto;
	}

	public List<ParamFieldDto> getParamFields() {
        return paramFields;
    }

    public void setParamFields(List<ParamFieldDto> paramFields) {
        this.paramFields = paramFields;
    }

    public List<Long> getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(List<Long> attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

}
