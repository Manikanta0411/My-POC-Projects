package com.record.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.record.model.Attachment;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "attachment_entity_link_tbl")
//@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class AttachmentEntityLink extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attachment_id", nullable = false)
	private Attachment attachment;
	private String entityIds;
	
}
