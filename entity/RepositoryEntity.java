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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "repository")
@EntityListeners(AuditingEntityListener.class)
public class RepositoryEntity extends BaseModel {

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	private String title;
	
	private Boolean status;
	
	@Column(name = "REPO_CODE")
	private String repositoryCode;

}
