package com.record.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.record.model.LastOpIndicator;

import lombok.Data;
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@CreatedDate
	@DateTimeFormat(pattern = "MM.dd.yyyy HH:mm:ss")
	@JsonFormat(pattern = "MM.dd.yyyy HH:mm:ss")
	@Column(name = "CREATED_DATE", updatable = false)

	public Date createdDate;

	@LastModifiedDate
	@DateTimeFormat(pattern = "MM.dd.yyyy HH:mm:ss")
	@JsonFormat(pattern = "MM.dd.yyyy HH:mm:ss")
	@Column(name = "UPDATED_DATE")
	public Date updatedDate;

	@Column(name = "LAST_OP_IND", length = 10)
	@Enumerated(EnumType.STRING)
	private LastOpIndicator lastOpIndicator;

	@Column(name = "CREATED_BY", length = 10)
	private String createdBy;

	@Column(name = "LAST_OP_BY", length = 10)
	private String lastUpdatedBy;

}
