package com.record.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.record.entity.LobEntity;
import com.record.exception.BadRequestException;
import com.record.exception.ResourceNotFoundException;
import com.record.model.LastOpIndicator;
import com.record.model.LobModel;
import com.record.repository.BusinessRepository;
import com.record.repository.LobRepository;
import com.record.util.MapperUtil;

@Service
public class LobService {

	private LobRepository lobRepository;
	private BusinessRepository businessRepository;

	public LobService(LobRepository lobRepository,BusinessRepository businessRepository) {
		this.lobRepository = lobRepository;
		this.businessRepository=businessRepository;
	}

	public LobModel createLob(LobModel lob) throws BadRequestException {
		LobEntity lobEntity = MapperUtil.convertModelToEntityStrict(lob, LobEntity.class);
		lobEntity.setLastOpIndicator(LastOpIndicator.I);
		if (lobRepository.findByLobCode(lobEntity.getLobCode()).isPresent()) {
			throw new BadRequestException("Lob Code " + lobEntity.getLobCode() + " should be unique!");
		}
		return MapperUtil.convertEntityToModel(lobRepository.save(lobEntity), LobModel.class);
	}
	
	
	public LobModel updateLob(Long lobId, LobModel lobModel) throws EntityNotFoundException, BadRequestException {

		LobEntity lobEntity = findById(lobId);
		lobEntity.setLobName(lobModel.getLobName());
		lobEntity.setStatus(lobModel.getStatus());
		lobEntity.setLastOpIndicator(LastOpIndicator.U);
		lobEntity.setExpireDate(lobModel.getExpireDate());
		LobEntity lob = lobRepository.save(lobEntity);
		return MapperUtil.convertEntityToModel(lob, LobModel.class);
	}
	
	
	public void deleteLob(Long lobId) {
		LobEntity lobEntity = findById(lobId);
		if (lobEntity != null) {
			lobEntity.setLastOpIndicator(LastOpIndicator.D);
			lobEntity.setStatus(false);
			lobRepository.save(lobEntity);
		}
	}
	

	public List<LobModel> getAllLobs(String status) {

		if (status.equalsIgnoreCase("all")) {
			return lobRepository.findAll().stream()
					.map(actionEntity -> MapperUtil.convertEntityToModel(actionEntity, LobModel.class))
					.collect(Collectors.toList());
		} else if (status.equalsIgnoreCase("true")) {
			List<LobEntity> entitiesList = lobRepository.findByStatusTrue();
			if(entitiesList.isEmpty()) {
				throw new ResourceNotFoundException("lob not found for status "+status);
			}
			return entitiesList.stream()
					.map(actionEntity -> MapperUtil.convertEntityToModel(actionEntity, LobModel.class))
					.collect(Collectors.toList());
		} else {
			List<LobEntity> entitiesList = lobRepository.findByStatusFalse();
			if(entitiesList.isEmpty()) {
				throw new ResourceNotFoundException("lob not found for status "+status);
			}
			return entitiesList.stream()
					.map(actionEntity -> MapperUtil.convertEntityToModel(actionEntity, LobModel.class))
					.collect(Collectors.toList());

		}

	}

	
	public LobEntity findById(Long lobId) throws EntityNotFoundException {
		Optional<LobEntity> lobEntity = lobRepository.findById(lobId);
		if (!lobEntity.isPresent()) {
			throw new EntityNotFoundException("Lob with id " + lobId + " not found");
		}
		return lobEntity.get();
	}
	
	
	public LobModel findBylobCode(String lobCode) {
		return MapperUtil.convertEntityToModel(findByCode(lobCode), LobModel.class);
	}

	public LobEntity findByCode(String lobCode) throws EntityNotFoundException {
		Optional<LobEntity> lobEntity = lobRepository.findByLobCode(lobCode);
		if (!lobEntity.isPresent()) {
			throw new EntityNotFoundException("Lob with lob code " + lobCode + " not found!");
		}
		return lobEntity.get();
	}

	
}
