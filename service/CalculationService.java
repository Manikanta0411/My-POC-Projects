package com.record.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.record.entity.BusinessEntityOp;
import com.record.entity.CalculationEntity;
import com.record.exception.BadRequestException;
import com.record.model.BusinessEntityEnum;
import com.record.model.BusinessModel;
import com.record.model.CalculationModel;
import com.record.model.EntityType;
import com.record.model.LastOpIndicator;
import com.record.repository.BusinessRepository;
import com.record.repository.CalculationRepository;
import com.record.util.GenericSpesification;
import com.record.util.MapperUtil;
import com.record.util.SearchCriteria;
import com.record.util.SearchOperation;

@Service
public class CalculationService {


	private CalculationRepository calculationRepository;
	private BusinessRepository businessRepository;
	
	@Autowired
	private SystemParameterService parameterService;
	
	public CalculationService( CalculationRepository calculationRepository,BusinessRepository businessRepository) {
		this.calculationRepository = calculationRepository;
		this.businessRepository = businessRepository;
		
	}

	public CalculationModel createCalculation(CalculationModel calculationModel) throws BadRequestException {
		CalculationEntity calculationEntity = MapperUtil.convertModelToEntityStrict(calculationModel,
				CalculationEntity.class);
//		Optional<BusinessEntityOp> businessEntity = businessRepository
//				.findByEntityId(calculationModel.getBusinessModel().getEntityId());
//		if (businessEntity.isPresent()) {
//			calculationEntity.setBusinessEntity(businessEntity.get());
//		} else {
//			throw new BadRequestException(
//					"Business Entity Id " + calculationModel.getBusinessModel().getEntityId() + " not exists!");
//		}
		calculationEntity.setLastOpIndicator(LastOpIndicator.I);
		calculationEntity.setCalculationCode("CCD_"+parameterService.getCalcSequence());
		try{
			CalculationEntity savedEntity = calculationRepository.save(calculationEntity);
			return MapperUtil.convertEntityToModel(savedEntity, CalculationModel.class);
		}catch(DataIntegrityViolationException e)
		{
			throw new BadRequestException(
					"Tax Name with Entity already Exists!");
		}
		
		
	}
	
	public CalculationModel updateCalculation(String calculationCode,CalculationModel model) {
		
		CalculationEntity calculationEntity = findByCode(calculationCode);
		calculationEntity.setField1Name(model.getField1Name());
		calculationEntity.setField1Type(model.getField1Type());
		calculationEntity.setField2Name(model.getField2Name());
		calculationEntity.setField2Type(model.getField2Type());
		calculationEntity.setField3Name(model.getField3Name());
		calculationEntity.setField3Type(model.getField3Type());
		calculationEntity.setField4Name(model.getField4Name());
		calculationEntity.setField4Type(model.getField4Type());
		calculationEntity.setField5Name(model.getField5Name());
		calculationEntity.setField5Type(model.getField5Type());
		calculationEntity.setOperation(model.getOperation());
		calculationEntity.setTaxName(model.getTaxName());
		CalculationEntity entity=calculationRepository.save(calculationEntity);
		
		return MapperUtil.convertEntityToModel(entity, CalculationModel.class);
	}
	
	public BusinessModel updateBusniessEntity(Long id, BusinessModel reqModel) throws EntityNotFoundException {

		BusinessEntityOp businessEntity = findById(id);
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
	
	public CalculationEntity findByCode(String code)throws EntityNotFoundException{
		Optional<CalculationEntity> calculationEntity = calculationRepository.findByCalculationCode(code);
		if(!calculationEntity.isPresent()) {
		
			throw new EntityNotFoundException("Calculation Entity with code "+code +" not found");
		}
		return calculationEntity.get();
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
