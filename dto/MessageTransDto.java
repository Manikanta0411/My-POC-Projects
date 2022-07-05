package com.record.dto;

import java.util.UUID;

public class MessageTransDto {

    private Long id;
    private String message;
    private String originMessageId;
    private Long layerId;
    private Long riskId;
    private String senderUser;
    private String senderEntityId;
    private String messageStatus;
    private Boolean isOwner;
    
    

    public String getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}

	public Boolean getIsOwner() {
		return isOwner;
	}

	public void setIsOwner(Boolean isOwner) {
		this.isOwner = isOwner;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOriginMessageId() {
        return originMessageId;
    }

    public void setOriginMessageId(String originMessageId) {
        this.originMessageId = originMessageId;
    }

    public Long getRiskId() {
        return riskId;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }

    public Long getLayerId() {
        return layerId;
    }

    public void setLayerId(Long layerId) {
        this.layerId = layerId;
    }

    public String getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(String senderUser) {
        this.senderUser = senderUser;
    }

    public String getSenderEntityId() {
        return senderEntityId;
    }

    public void setSenderEntityId(String senderEntityId) {
        this.senderEntityId = senderEntityId;
    }
}
