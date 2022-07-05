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

import com.record.model.BusinessEntity;
import com.record.model.Layer;
import com.record.model.Risk;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "param_entity_link_tbl")
//@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class ParamEntityLink extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6274257558097454165L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String fieldIds;
	private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LAYER_ID", nullable = false)
    private Layer layer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RISK_ID", nullable = false)
    private Risk risk;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTITY_ID", nullable = false)
    private BusinessEntityOp businessEntity;
    

}
