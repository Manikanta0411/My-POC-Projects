package com.record.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.record.entity.BusinessEntityOp;
import com.record.entity.CalculationEntity;
import com.record.entity.TaxEntity;
import com.record.exception.BadRequestException;
import com.record.model.BusinessEntityEnum;
import com.record.model.BusinessModel;
import com.record.model.EntityType;
import com.record.model.LastOpIndicator;
import com.record.model.TaxModel;
import com.record.repository.BusinessRepository;
import com.record.repository.CalculationRepository;
import com.record.repository.TaxRepository;
import com.record.util.GenericSpesification;
import com.record.util.MapperUtil;
import com.record.util.SearchCriteria;
import com.record.util.SearchOperation;

@Service
public class TaxService {


	private TaxRepository taxRepository;
	private CalculationRepository calculationRepository;
	private BusinessRepository businessRepository;
	
	@Autowired
	private SystemParameterService parameterService;
	
	public TaxService( CalculationRepository calculationRepository,BusinessRepository businessRepository,TaxRepository taxRepository) {
		this.calculationRepository = calculationRepository;
		this.businessRepository = businessRepository;
		this.taxRepository=taxRepository;
		
	}

	public TaxModel createTax(TaxModel taxModel) throws BadRequestException {
		TaxEntity taxEntity = MapperUtil.convertModelToEntityStrict(taxModel, TaxEntity.class);
		Optional<BusinessEntityOp> businessEntity = businessRepository
				.findByEntityId(taxModel.getBusinessEntityId());
		if (businessEntity.isPresent()) {
			taxEntity.setBusinessEntity(businessEntity.get());
		} else {
			throw new BadRequestException(
					"Business Entity Id " + taxModel.getBusinessEntityId() + " not exists!");
		}

		Optional<CalculationEntity> calculationEntity = calculationRepository
				.findByCalculationCode(taxModel.getCalculationCode());
		
		if (calculationEntity.isPresent()) {
			taxEntity.setCalculationEntity(calculationEntity.get());
		} else {
			throw new BadRequestException("Calculation Code " + taxModel.getCalculationCode() + " not exists!");
		}
		taxEntity.setLastOpIndicator(LastOpIndicator.I);
		taxEntity.setTaxCode("TAX_"+parameterService.getTaxSequence());
		try {
			TaxEntity savedEntity = taxRepository.save(taxEntity);
			return MapperUtil.convertEntityToModel(savedEntity, TaxModel.class);
		} catch (DataIntegrityViolationException e) {
			throw new BadRequestException("Tax Name with Entity already Exists!");
		}

	}

	public TaxModel updateTaxEntity(String taxCode,TaxModel model) throws EntityNotFoundException{
		TaxEntity taxEntity = findByTaxCode(taxCode);
		taxEntity.setTaxName(model.getTaxName());
		taxEntity.setRate(model.getRate());
		taxEntity.setRegNo(model.getRegNo());
		taxEntity.setTdsWthTax(model.getTdsWthTax());
		taxEntity.setIsActive(model.getIsActive());
		taxEntity.setIsNegative(model.getIsNegative());
		taxEntity.setIsTaxUnderRevCharge(model.getIsTaxUnderRevCharge());
		taxEntity.setIsTdsWthReq(model.getIsTdsWthReq());
	
		TaxEntity updatedTaxEntity = taxRepository.save(taxEntity);
		
		return MapperUtil.convertEntityToModel(updatedTaxEntity, TaxModel.class);
		
	}
	
	public TaxEntity findByTaxCode(String taxCode)throws EntityNotFoundException {
		Optional<TaxEntity> taxEntity = taxRepository.findByTaxCode(taxCode);
		if (!taxEntity.isPresent()) {
			throw new EntityNotFoundException("tax with taxCode " + taxCode + " not found!");
		}
		return taxEntity.get();
	}
	
	public void deleteTax(String taxCode) {
		TaxEntity taxEntity = findByTaxCode(taxCode);
		if (taxEntity != null) {
			taxEntity.setLastOpIndicator(LastOpIndicator.D);
			taxEntity.setIsActive(false);
			taxRepository.save(taxEntity);
		}
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
		return MapperUtil.convertEntityToModel(updatedBusinessEntity, BusinessModel.class);
		}
	
	public BusinessEntityOp findById(Long id) throws EntityNotFoundException {
		Optional<BusinessEntityOp> businessEntity = businessRepository.findById(id);
		if (!businessEntity.isPresent()) {
			throw new EntityNotFoundException("Busniess Entity with Id " + id + " not found!");
		}
		return businessEntity.get();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<TaxModel> getAllTax(String entityCode) {
		
		Optional<BusinessEntityOp> businessEntity= businessRepository.findByEntityId(entityCode);
		if(businessEntity.isPresent())
		{
			List<TaxEntity> taxes= taxRepository.findByBusinessEntity(businessEntity.get());
		
		if(taxes.isEmpty()) {
			throw new EntityNotFoundException("No Tax exist for entityId "+entityCode);
		}
			return taxes.stream()
			.map(taxEntity -> MapperUtil.convertEntityToModel(taxEntity, TaxModel.class))
			.collect(Collectors.toList());
			
		}else
		{
			throw new EntityNotFoundException("Business Entity with Id " + entityCode + " not found!");
		}
		
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
