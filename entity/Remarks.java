package com.record.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.record.model.BaseModel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "remarks")
//@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Remarks extends BaseModel{

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "REMARKS", columnDefinition="TEXT")
    private String remarks;

	@Column(name = "LAYER_ID")
	private Long layerId;
	
	@Column(name = "HEADING_ID")
	private Long headingId;
	
	private String remarksCode;
	private boolean status;
	
}
