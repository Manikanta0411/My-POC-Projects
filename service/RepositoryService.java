package com.record.service;

import java.util.Date;
import java.util.Objects;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.record.constant.LastOpIndicator;
import com.record.entity.RepositoryEntity;
import com.record.exception.BadRequestException;
import com.record.model.RepositoryModel;
import com.record.repository.RepoRepository;
import com.record.util.MapperUtil;

@Service
public class RepositoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LayerService.class);

	@Autowired
	private RepoRepository repoRepository;
<<<<<<< HEAD
	
	@Autowired
	private SystemParameterService parameterService;

	public RepositoryModel createRepository(RepositoryModel repositoryModel) throws BadRequestException {

		RepositoryEntity repositoryEntity = MapperUtil.convertModelToEntity(repositoryModel, RepositoryEntity.class);
		if (repoRepository.findByTitle(repositoryEntity.getTitle()).isPresent()) {
			throw new BadRequestException("Title " + repositoryEntity.getTitle() + " should be unique");
		}
		repositoryEntity.setLastOpIndicator(LastOpIndicator.C);
		repositoryEntity.setCreatedDate(new Date());
		repositoryEntity.setUpdatedDate(new Date());
		repositoryEntity.setStatus(true);
		repositoryEntity.setRepositoryCode("REPO_"+parameterService.getRepoSequence());
		RepositoryEntity repo = repoRepository.save(repositoryEntity);
		return MapperUtil.convertEntityToModel(repo, RepositoryModel.class);
	}

	public RepositoryEntity findById(Long repoId) throws EntityNotFoundException {
		RepositoryEntity repositoryEntity = repoRepository.findById(repoId).orElse(null);
		if (Objects.isNull(repositoryEntity)) {
			throw new EntityNotFoundException("Role with id " + repoId + " not found");
		}
		return repositoryEntity;
	}
	
	public RepositoryEntity findByCode(String repoCode) throws EntityNotFoundException {
		RepositoryEntity repositoryEntity = repoRepository.findByRepoCode(repoCode).orElse(null);
		if (Objects.isNull(repositoryEntity)) {
			throw new EntityNotFoundException("Role with id " + repoCode + " not found");
		}
		return repositoryEntity;
	}
	
	public RepositoryModel findByRepoId(Long repoId) {
		return MapperUtil.convertEntityToModel(findById(repoId), RepositoryModel.class);
	}
	
	public RepositoryModel findByRepoCode(String repoCode) {
		return MapperUtil.convertEntityToModel(findByCode(repoCode), RepositoryModel.class);
	}

	public RepositoryModel updateRepository(Long repoId, RepositoryModel repoModel) throws EntityNotFoundException {
		RepositoryEntity entity = findById(repoId);
		entity.setStatus(repoModel.getStatus());
		entity.setTitle(repoModel.getTitle());
		entity.setLastOpIndicator(LastOpIndicator.U);
		entity.setCreatedDate(new Date());
		entity.setUpdatedDate(new Date());
		RepositoryEntity repoEntity = repoRepository.save(entity);

		return MapperUtil.convertEntityToModel(repoEntity, RepositoryModel.class);
	}
	
	public RepositoryModel updateRepositoryByCode(String repositoryCode, RepositoryModel repoModel) throws EntityNotFoundException {
		RepositoryEntity entity = findByCode(repositoryCode);
		entity.setStatus(repoModel.getStatus());
		entity.setTitle(repoModel.getTitle());
		entity.setLastOpIndicator(LastOpIndicator.U);
		entity.setCreatedDate(new Date());
		entity.setUpdatedDate(new Date());
		RepositoryEntity repoEntity = repoRepository.save(entity);

		return MapperUtil.convertEntityToModel(repoEntity, RepositoryModel.class);
	}

	public void deleteRepository(Long repoId) {
		RepositoryEntity repositoryEntity = findById(repoId);
		if (repositoryEntity != null) {
			repositoryEntity.setLastOpIndicator(LastOpIndicator.D);
			repositoryEntity.setStatus(false);
		}
	}

	public void deleteRepositoryByCode(String repoCode) {
		RepositoryEntity repositoryEntity = findByCode(repoCode);
		if (repositoryEntity != null) {
			repositoryEntity.setLastOpIndicator(LastOpIndicator.D);
			repositoryEntity.setStatus(false);
		}
	}
=======

	public RepositoryModel createRepository(RepositoryModel repositoryModel) throws BadRequestException {

		RepositoryEntity repositoryEntity = MapperUtil.convertModelToEntity(repositoryModel, RepositoryEntity.class);
		if (repoRepository.findByTitle(repositoryEntity.getTitle()).isPresent()) {
			throw new BadRequestException("Title " + repositoryEntity.getTitle() + " should be unique");
		}
		repositoryEntity.setLastOpIndicator(LastOpIndicator.C);
		repositoryEntity.setCreatedDate(new Date());
		repositoryEntity.setUpdatedDate(new Date());
		RepositoryEntity repo = repoRepository.save(repositoryEntity);
		return MapperUtil.convertEntityToModel(repo, RepositoryModel.class);
	}

	public RepositoryEntity findById(Long repoId) throws EntityNotFoundException {
		RepositoryEntity repositoryEntity = repoRepository.findById(repoId).orElse(null);
		if (Objects.isNull(repositoryEntity)) {
			throw new EntityNotFoundException("Role with id " + repoId + " not found");
		}
		return repositoryEntity;
	}
	
	public RepositoryModel findByRepoId(Long repoId) {
		return MapperUtil.convertEntityToModel(findById(repoId), RepositoryModel.class);
	}

	public RepositoryModel updateRepository(Long repoId, RepositoryModel repoModel) throws EntityNotFoundException {
		RepositoryEntity entity = findById(repoId);
		entity.setStatus(repoModel.getStatus());
		entity.setTitle(repoModel.getTitle());
		entity.setLastOpIndicator(LastOpIndicator.U);
		entity.setCreatedDate(new Date());
		entity.setUpdatedDate(new Date());
		RepositoryEntity repoEntity = repoRepository.save(entity);

		return MapperUtil.convertEntityToModel(repoEntity, RepositoryModel.class);
	}

	public void deleteRepository(Long repoId) {
		RepositoryEntity repositoryEntity = findById(repoId);
		if (repositoryEntity != null) {
			repositoryEntity.setLastOpIndicator(com.record.constant.LastOpIndicator.D);
			repositoryEntity.setStatus(false);
		}
	}

>>>>>>> branch 'Mani_work_June8th2022' of https://github.com/RE-cord/Record_Backend_API.git
}
