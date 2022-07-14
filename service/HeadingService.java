package com.record.service;

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.record.constant.LastOpIndicator;
import com.record.entity.HeadingEntity;
<<<<<<< HEAD
import com.record.entity.RepositoryEntity;
import com.record.exception.BadRequestException;
import com.record.model.HeadingModel;
import com.record.repository.HeadingRepository;
import com.record.repository.RepoRepository;
import com.record.util.MapperUtil;

@Service
public class HeadingService {

	@Autowired
	private HeadingRepository headingRepository;

	@Autowired
	private SystemParameterService parameterService;
	
	@Autowired
	private RepoRepository repoRepository;

	public HeadingModel createHeading(HeadingModel headingModel) throws EntityNotFoundException, BadRequestException {
		HeadingEntity entity1 = MapperUtil.convertModelToEntity(headingModel, HeadingEntity.class);

		Optional<HeadingEntity> entity2 = headingRepository.findByHeadingAndRepoId(headingModel.getHeading(),
				headingModel.getRepoId());
		Optional<RepositoryEntity> repositoryEntity = repoRepository.findById(headingModel.getRepoId());
		if(repositoryEntity.isPresent()) {
			if (entity2.isPresent()) {
				throw new BadRequestException("Heading with " + " ( " + entity1.getHeading() + " ) " + " and Repo id "
						+ entity1.getRepoId() + " should be unique");
			} else {
				entity1.setCreatedDate(new Date());
				entity1.setUpdatedDate(new Date());
				entity1.setLastOpIndicator(LastOpIndicator.C);
				entity1.setStatus(true);
				entity1.setHeadingCode("HEAD_" + parameterService.getHeadingSequence());
				HeadingEntity entity3 = headingRepository.save(entity1);
				return MapperUtil.convertEntityToModel(entity3, HeadingModel.class);
			}
		}else {
			throw new EntityNotFoundException("Repository with id "+headingModel.getRepoId()+" not found");
		}
		
	}

	public HeadingModel findByHeadingId(Long headingId) {
		return MapperUtil.convertEntityToModel(findById(headingId), HeadingModel.class);
	}

	public HeadingEntity findById(Long id) {
		Optional<HeadingEntity> entity = headingRepository.findById(id);
		if (!entity.isPresent()) {
			throw new EntityNotFoundException("Heading with id " + id + " not found");
		}
		return entity.get();
	}

	public HeadingEntity findByCode(String code) {
		Optional<HeadingEntity> entity = headingRepository.findByHeadingCode(code);
		if (!entity.isPresent()) {
			throw new EntityNotFoundException("Heading with id " + code + " not found");
		}
		return entity.get();
	}

	public HeadingModel findByHeadingCode(String headingCode) {
		return MapperUtil.convertEntityToModel(findByCode(headingCode), HeadingModel.class);
	}

	public HeadingModel updateHeading(Long id, HeadingModel headingModel) throws BadRequestException {

		HeadingEntity entity = findById(id);
		if (entity != null) {
			Optional<HeadingEntity> entity2 = headingRepository.findByHeadingAndRepoId(headingModel.getHeading(),
					headingModel.getRepoId());
			if (entity2.isPresent()) {

				if (!entity2.get().getId().equals(entity.getId())) {
					throw new BadRequestException("Heading with " + " ( " + entity.getHeading() + " ) "
							+ " and Repo id " + entity.getRepoId() + " should be unique");

				} else {

					entity.setDescription(headingModel.getDescription());
					entity.setHeading(headingModel.getHeading());
					entity.setCreatedDate(new Date());
					entity.setUpdatedDate(new Date());
					HeadingEntity headingEntity = headingRepository.save(entity2.get());
					return MapperUtil.convertEntityToModel(headingEntity, HeadingModel.class);
				}
			} else {
				entity.setDescription(headingModel.getDescription());
				entity.setHeading(headingModel.getHeading());
				entity.setCreatedDate(new Date());
				entity.setUpdatedDate(new Date());
				HeadingEntity headingEntity = headingRepository.save(entity2.get());
				return MapperUtil.convertEntityToModel(headingEntity, HeadingModel.class);
			}
		} else {
			throw new EntityNotFoundException("Heading with id " + id + " not found");

		}

	}

	public HeadingModel updateHeadingByCode(String code,HeadingModel headingModel) throws BadRequestException {

		HeadingEntity entity = findByCode(code);
		if (entity != null) {
			Optional<HeadingEntity> entity2 = headingRepository.findByHeadingAndRepoId(headingModel.getHeading(),
					headingModel.getRepoId());
			if (entity2.isPresent()) {

				if (!entity2.get().getId().equals(entity.getId())) {
					throw new BadRequestException("Heading with " + " ( " + entity.getHeading() + " ) "
							+ " and Repo id " + entity.getRepoId() + " should be unique");

				} else {

					entity.setDescription(headingModel.getDescription());
					entity.setHeading(headingModel.getHeading());
					entity.setCreatedDate(new Date());
					entity.setUpdatedDate(new Date());
					HeadingEntity headingEntity = headingRepository.save(entity2.get());
					return MapperUtil.convertEntityToModel(headingEntity, HeadingModel.class);
				}
			} else {
				entity.setDescription(headingModel.getDescription());
				entity.setHeading(headingModel.getHeading());
				entity.setCreatedDate(new Date());
				entity.setUpdatedDate(new Date());
				HeadingEntity headingEntity = headingRepository.save(entity2.get());
				return MapperUtil.convertEntityToModel(headingEntity, HeadingModel.class);
			}
		} else {
			throw new EntityNotFoundException("Heading with code " + code + " not found");

		}
		
	}

	public void deleteHeading(Long headingId) {
		HeadingEntity headingEntity = findById(headingId);
		if (headingEntity != null) {
			headingEntity.setLastOpIndicator(LastOpIndicator.D);
			headingEntity.setStatus(false);
		}
	}

	public void deleteHeadingByCode(String headingCode) {
		HeadingEntity headingEntity = findByCode(headingCode);
		if (headingEntity != null) {
			headingEntity.setLastOpIndicator(LastOpIndicator.D);
			headingEntity.setStatus(false);
		}
	}
=======
import com.record.model.HeadingModel;
import com.record.repository.HeadingRepository;
import com.record.util.MapperUtil;

@Service
public class HeadingService {

	@Autowired
	private HeadingRepository headingRepository;
	
	public HeadingModel createHeading(HeadingModel headingModel) throws EntityNotFoundException {
		HeadingEntity entity1 = MapperUtil.convertModelToEntity(headingModel, HeadingEntity.class);
		
		Optional<HeadingEntity> entity2 = headingRepository.findByHeadingAndRepoId(headingModel.getHeading() , headingModel.getRepoId());
		
		if(entity2.isPresent()) {
			throw new EntityNotFoundException("Heading with " +" ( " + entity1.getHeading() + " ) " +" and Repo id " +entity1.getRepoId()+ " should be unique");
		}else {
		entity1.setCreatedDate(new Date());
		entity1.setUpdatedDate(new Date());
		entity1.setLastOpIndicator(LastOpIndicator.C);
		HeadingEntity entity3 = headingRepository.save(entity1);
		return MapperUtil.convertEntityToModel(entity3, HeadingModel.class);
	}
	}
	
	public HeadingModel findByHeadingId(Long headingId) {
		return MapperUtil.convertEntityToModel(findById(headingId), HeadingModel.class);
		}
	
	public HeadingEntity findById(Long id) {
		Optional<HeadingEntity> entity = headingRepository.findById(id);
		if(!entity.isPresent()) {
			throw new EntityNotFoundException("Heading with id "+id+" not found");
		}
		return entity.get();
	}

	public HeadingModel updateHeading(Long id,HeadingModel headingModel) throws EntityNotFoundException{
		HeadingEntity entity = findById(id);
		
		entity.setDescription(headingModel.getDescription());
		entity.setHeading(headingModel.getHeading());
		entity.setLastOpIndicator(LastOpIndicator.U);
		entity.setCreatedDate(new Date());
		entity.setUpdatedDate(new Date());
		HeadingEntity headingEntity = headingRepository.save(entity);
		return MapperUtil.convertEntityToModel(headingEntity, HeadingModel.class);
		
	}
	
	public HeadingModel updateHeading1(Long id,HeadingModel headingModel) {
		HeadingEntity entity1 = MapperUtil.convertModelToEntity(headingModel, HeadingEntity.class);
		
		Optional<HeadingEntity> entity2 = headingRepository.findByHeadingAndRepoId(headingModel.getHeading() , headingModel.getRepoId());
		if(entity2.isPresent()) {
			throw new EntityNotFoundException("Heading with " +" ( " + entity1.getHeading() + " ) " +" and Repo id " +entity1.getRepoId()+ " should be unique");
		}else {
			entity1.setHeading(headingModel.getHeading());
			entity1.setDescription(headingModel.getDescription());
			entity1.setLastOpIndicator(LastOpIndicator.U);
			entity1.setCreatedDate(new Date());
			entity1.setUpdatedDate(new Date());
			HeadingEntity headingEntity = headingRepository.save(entity1);
			return MapperUtil.convertEntityToModel(headingEntity, HeadingModel.class);
		}
		
	}
	
	public void deleteHeading(Long headingId) {
		HeadingEntity headingEntity = findById(headingId);
		if (headingEntity != null) {
			headingEntity.setLastOpIndicator(com.record.constant.LastOpIndicator.D);
			headingEntity.setStatus(false);
		}
	}

>>>>>>> branch 'Mani_work_June8th2022' of https://github.com/RE-cord/Record_Backend_API.git
}
