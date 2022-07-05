package com.record.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.record.entity.HeadingEntity;

public interface HeadingRepository  extends JpaRepository<HeadingEntity, Long>{

	public Optional<HeadingEntity> findByHeadingAndRepoId(String heading,Long repoId);
	
	public Optional<HeadingEntity> findByHeadingCode(String string);
	
}
