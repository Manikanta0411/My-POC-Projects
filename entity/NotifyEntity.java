package com.record.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "notify")
//@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class NotifyEntity extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2098040261254446502L;
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Long id;
	private String originMessageId;
	private String entityId;
	private Boolean isRead;
	private Boolean	isSentByMe;
	}
