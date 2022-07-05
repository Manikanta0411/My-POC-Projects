package com.record.model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "MESSAGE_TRANS")
public class MessageTrans {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "MESSAGE", columnDefinition="TEXT")
    private String message;
    @Column(name = "MESSAGE_DATETIME")
    private Date messageDatetime;
    @Column(name = "SENDER_ENTITY_ID")
    private String senderEntityId;
    @Column(name = "RECEIVER_ENTITY_ID")
    private Long receiverEntityId;
    @Column(name = "SENDER_NAME")
    private String senderName;
    @Column(name = "riskId")
    private Long riskId;
    @Column(name = "layerId")
    private Long layerId;
    @Column(name = "MESSAGE_STATUS")
    private String messageStatus;
    @Column(name = "ORIGIN_MESSAGE_ID")
    private String originMessageId;
    @Column(name = "messageType")
    private String messageType;
    private Boolean isOwner;
    
    

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

    public Date getMessageDatetime() {
        return messageDatetime;
    }

    public void setMessageDatetime(Date messageDatetime) {
        this.messageDatetime = messageDatetime;
    }

    public Long getReceiverEntityId() {
        return receiverEntityId;
    }

    public void setReceiverEntityId(Long receiverEntityId) {
        this.receiverEntityId = receiverEntityId;
    }

    public String getSenderEntityId() {
        return senderEntityId;
    }

    public void setSenderEntityId(String senderEntityId) {
        this.senderEntityId = senderEntityId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getRiskId() {
        return riskId;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Long getLayerId() {
        return layerId;
    }

    public void setLayerId(Long layerId) {
        this.layerId = layerId;
    }

    public String getOriginMessageId() {
        return originMessageId;
    }

    public void setOriginMessageId(String originMessageId) {
        this.originMessageId = originMessageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
