package com.record.entity;

import java.io.Serializable;

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
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tax",uniqueConstraints = {
		   @UniqueConstraint(name = "UniqueTaxNameAndEntityId", columnNames = {"taxName", "business_entity_id"})})
//@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class TaxEntity extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1951987527862731332L;
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String taxCode;
	private String taxName;
	private String regNo;
	private Long rate;

	private Long tdsWthTax;
	private Boolean isTdsWthReq;
	private Boolean isTaxUnderRevCharge;
	private Boolean isActive;
	private Boolean isNegative;
	@JsonBackReference
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@Valid
	@JoinColumn(name = "calculation_id")
	private CalculationEntity calculationEntity;
	@JsonBackReference
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@Valid
	@JoinColumn(name = "business_entity_id")
	private BusinessEntityOp businessEntity;
	
	
}
