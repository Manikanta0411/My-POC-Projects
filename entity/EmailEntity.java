package com.record.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "email")
@EntityListeners(AuditingEntityListener.class)
public class EmailEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String email;
	private Boolean status;
	private String salutation;
	private Boolean claim;
	private Boolean underwriting;
	private Boolean accounting;
	@DateTimeFormat(pattern="MM.dd.yyyy HH:mm:ss")
	@JsonFormat(pattern = "MM.dd.yyyy HH:mm:ss")
	private Date expireDate;
	private String lobCode;
	@JsonBackReference
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@Valid
	@JoinColumn(name = "business_entity_id")
	private BusinessEntityOp businessEntity;
}
