package com.record.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.record.entity.BusinessEntityOp;
import com.record.exception.BadRequestException;
import com.record.model.BusinessEntityEnum;
import com.record.model.BusinessModel;
import com.record.model.EntityType;
import com.record.model.LastOpIndicator;
import com.record.repository.BusinessRepository;
import com.record.util.GenericSpesification;
import com.record.util.MapperUtil;
import com.record.util.SearchCriteria;
import com.record.util.SearchOperation;

@Service
public class BusinessService {

	private BusinessRepository businessRepository;

	public BusinessService(BusinessRepository businessRepository) {
		this.businessRepository = businessRepository;
	}

	public BusinessModel createBusinessEntity(BusinessModel businessModel) throws BadRequestException {
		BusinessEntityOp businessEntity = MapperUtil.convertModelToEntityStrict(businessModel, BusinessEntityOp.class);
		if (businessRepository.findByEntityId(businessEntity.getEntityId()).isPresent()) {
			throw new BadRequestException("Entity already present!");
		}
		
		businessEntity.setEntityType(EntityType.valueFrom(businessModel.getEntityType().toString()).getValue());
		businessEntity.setLastOpIndicator(LastOpIndicator.I);
		BusinessEntityOp savedbusinessEntity=businessRepository.save(businessEntity);
		savedbusinessEntity.setEntityType(businessModel.getEntityType().toString());
		return MapperUtil.convertEntityToModel(savedbusinessEntity, BusinessModel.class);
	}
	
	public BusinessModel updateBusniessEntity(String entityId, BusinessModel reqModel) throws EntityNotFoundException {

		BusinessEntityOp businessEntity = findByEntityId(entityId);
		businessEntity.setBaseCurrency(reqModel.getBaseCurrency());
		businessEntity.setEntityAddress(reqModel.getEntityAddress());
		businessEntity.setEntityHostName(reqModel.getEntityHostName());
		businessEntity.setEntityHostPort(reqModel.getEntityHostPort());
		businessEntity.setEntityShortName(reqModel.getEntityShortName());
		businessEntity.setHomeCountry(reqModel.getHomeCountry());
		//businessEntity.setIncomeTaxIdentificationNumber(reqModel.getIncomeTaxIdentificationNumber());
		businessEntity.setPinCode(reqModel.getPinCode());
		//businessEntity.setRating(reqModel.getRating());
		//businessEntity.setRatingAgency(reqModel.getRatingAgency());
		businessEntity.setStateCode(reqModel.getStateCode());
		businessEntity.setTdsWhtRateApplicable(reqModel.getTdsWhtRateApplicable());
		businessEntity.setLastOpIndicator(LastOpIndicator.U);
		businessEntity.setEntitySubType(reqModel.getEntitySubType());
		BusinessEntityOp updatedBusinessEntity = businessRepository.save(businessEntity);
		updatedBusinessEntity.setEntityType(EntityType.keyFrom(updatedBusinessEntity.getEntityType().toString()).getKey());
		return MapperUtil.convertEntityToModel(updatedBusinessEntity, BusinessModel.class);	}
	
	public BusinessEntityOp findById(Long id) throws EntityNotFoundException {
		Optional<BusinessEntityOp> businessEntity = businessRepository.findById(id);
		if (!businessEntity.isPresent()) {
			throw new EntityNotFoundException("Busniess Entity with Id " + id + " not found!");
		}
		return businessEntity.get();
	}
	
	
	public BusinessEntityOp findByEntityId(String id) throws EntityNotFoundException {
		Optional<BusinessEntityOp> businessEntity = businessRepository.findByEntityId(id);
		if (!businessEntity.isPresent()) {
			throw new EntityNotFoundException("Busniess Entity with Id " + id + " not found!");
		}
		return businessEntity.get();
	}
	
	
		
	@SuppressWarnings("unchecked")
	public List<BusinessEntityOp> getAllBusinessEntity(BusinessEntityEnum key, Object value) {
		String entityKey= BusinessEntityEnum.valueFrom(key.getKey()).getValue();
		if ((entityKey.equalsIgnoreCase("status") && (value.equals("true")))) {
			value = true;
		}
		if ((entityKey.equalsIgnoreCase("status") && (value.equals("false")))) {
			value = false;
		}
		GenericSpesification<BusinessEntityOp> genericSpesification = new GenericSpesification<BusinessEntityOp>();
		genericSpesification.add(new SearchCriteria(entityKey, value, SearchOperation.EQUAL));
		return businessRepository.findAll(genericSpesification);
	}
 	
	
}