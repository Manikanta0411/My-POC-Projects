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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "heading")
@EntityListeners(AuditingEntityListener.class)
public class HeadingEntity extends BaseModel{

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "DESCRIPTION", columnDefinition="TEXT")
    private String description;

    @Column(name = "REPO_ID")
    private Long repoId;
    
    private boolean status;
    
    @Column(name = "HEADING")
    private String heading;
    
    private String headingCode;
    
}
