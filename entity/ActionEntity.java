package com.record.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "action")
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class ActionEntity extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 @Id
	 @GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length=50)
	private String actionCode;
	@Column(length=50)
	private String actionName;
	
	//@OneToOne(cascade = CascadeType.ALL)
	//@JoinColumn(name = "role_id")
	//private RoleEntity role;
	
	private Boolean canCreate;
	private Boolean canUpdate;
	private Boolean canDelete;
	private Boolean canRead;
	private Boolean status;
	private Date expireDate;
	
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "role_id", nullable = false)
	  private RoleEntity role;
	
	
	
}
