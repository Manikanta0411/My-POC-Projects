package com.record.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
@Getter
@Setter
@Entity
@Table(name = "calculation",uniqueConstraints = {
		@UniqueConstraint(name = "UniqueCalTaxNameAndEntityId", columnNames = {"taxName"})})
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)

public class CalculationEntity extends BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5491090478069752755L;
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String calculationCode;
	private String taxName;
	private String field1Type;
	private String field1Name;
	private String field2Type;
	private String Field2Name;
	private String field3Type;
	private String field3Name;
	private String field4Type;
	private String field4Name;
	private String field5Type;
	private String field5Name;
	private String operation;

}
