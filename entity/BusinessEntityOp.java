package com.record.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.record.model.Attachment;
import com.record.model.PPW;
import com.record.model.ParamField;

import lombok.Data;

@Data
@Entity
@Table(name = "business_entity")
//@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class BusinessEntityOp extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String entityId;
	private String entityLegalName;
	private String entityShortName;
	private String homeCountry;
	private Boolean status;
	private Date entityStartFy;
	private Date entityEndFy;
	private String baseCurrency;
	private String entityHostName;
	private String entityHostPort;
	private String entityAddress;
	private String entityType; 
	private String pinCode;
	private String stateCode;
	private String tdsWhtRateApplicable;
	private String entitySubType;
	
	@Transient
	@JsonIgnore
	private Collection<ParamField> paramFields;
	/*
	@JsonIgnore
	@ManyToMany(mappedBy = "businessEntities")
    private Collection<Attachment> attachments;
   */
	@JsonIgnore
    @ManyToMany(mappedBy = "businessEntities")
    private Collection<PPW> ppw;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "businessEntity", fetch = FetchType.LAZY)
    private List<ParamEntityLink> entityLink;
    
    @JsonIgnore
	public Long getId() {
		return id;
	}
	@JsonProperty
	public void setId(Long id) {
		this.id = id;
	}

}
