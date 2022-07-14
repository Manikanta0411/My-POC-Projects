package com.record.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.record.entity.BusinessEntityOp;
import com.record.entity.TaxDocumentEntity;
import com.record.exception.BadRequestException;
import com.record.exception.ResourceNotFoundException;
import com.record.model.LastOpIndicator;
import com.record.model.TaxDocumentModel;
import com.record.repository.BusinessRepository;
import com.record.repository.TaxDocumentRepository;
import com.record.util.MapperUtil;

@Service
public class TaxDocumentService {

	private final TaxDocumentRepository taxDocumentRepository;
	private final BusinessRepository businessRepository;

	public TaxDocumentService(TaxDocumentRepository taxDocumentRepository, BusinessRepository businessRepository) {
		this.taxDocumentRepository = taxDocumentRepository;
		this.businessRepository = businessRepository;
	}

	public TaxDocumentModel createTaxDocument(TaxDocumentModel taxDocumentModel) throws BadRequestException {

		TaxDocumentEntity taxDocumentEntity = MapperUtil.convertModelToEntityStrict(taxDocumentModel,
				TaxDocumentEntity.class);

		Optional<BusinessEntityOp> businessEntity = businessRepository
				.findByEntityId(taxDocumentModel.getBusinessEntityId());
		if (businessEntity.isPresent()) {
			taxDocumentEntity.setBusinessEntity(businessEntity.get());
		} else {
			throw new BadRequestException(
					"Business Entity Id " + taxDocumentModel.getBusinessEntityId() + " not exists!");
		}
		taxDocumentEntity.setLastOpIndicator(LastOpIndicator.I);
		return MapperUtil.convertEntityToModel(taxDocumentRepository.save(taxDocumentEntity), TaxDocumentModel.class);

	}

	public TaxDocumentModel updateTaxDoc(Long Id, TaxDocumentModel req) throws EntityNotFoundException {

		TaxDocumentEntity taxDocEntity = findById(Id);
		taxDocEntity.setDocumentName(req.getDocumentName());
		taxDocEntity.setEndDate(req.getEndDate());
		taxDocEntity.setIsRequired(req.getIsRequired());
		taxDocEntity.setStartDate(req.getStartDate());
		taxDocEntity.setLastOpIndicator(LastOpIndicator.U);

		return MapperUtil.convertEntityToModel(taxDocumentRepository.save(taxDocEntity), TaxDocumentModel.class);
	}

	public void deleteTaxDoc(Long id) {
		TaxDocumentEntity taxDocEntity = findById(id);
		if (taxDocEntity != null) {
			taxDocEntity.setLastOpIndicator(LastOpIndicator.D);
			taxDocEntity.setIsRequired(false);
			taxDocumentRepository.save(taxDocEntity);
		}
	}

	public List<TaxDocumentModel> getAllTaxDocs() {
		List<TaxDocumentEntity> entitiesList = taxDocumentRepository.findAll();
		if(entitiesList.isEmpty()) {
			throw new ResourceNotFoundException("No Tax Document Added yet,Please add ");
		}
		return entitiesList.stream()
				.map(taxDocEntity -> MapperUtil.convertEntityToModel(taxDocEntity, TaxDocumentModel.class))
				.collect(Collectors.toList());
	}

	public TaxDocumentEntity findById(Long id) throws EntityNotFoundException {
		Optional<TaxDocumentEntity> taxDocEntity = taxDocumentRepository.findById(id);
		if (!taxDocEntity.isPresent()) {
			throw new EntityNotFoundException("Tax Document with id " + id + " not found");
		}
		return taxDocEntity.get();
	}

	public TaxDocumentModel findByTaxDocId(Long id) {
		return MapperUtil.convertEntityToModel(findById(id), TaxDocumentModel.class);
	}
	
	
	public List<TaxDocumentModel> findByTaxDocByEntityId(String id)  {

		Optional<BusinessEntityOp> businessEntity = businessRepository.findByEntityId(id);
	
		if (businessEntity.isPresent()) {
			List<TaxDocumentEntity> entitiesList = taxDocumentRepository.findByBusinessEntity(businessEntity.get());
			if(entitiesList.isEmpty()) {
				throw new ResourceNotFoundException("No Tax Document exist with entity id "+id);
			}
			return entitiesList.stream()
					.map(taxDocEntity -> MapperUtil.convertEntityToModel(taxDocEntity, TaxDocumentModel.class))
					.collect(Collectors.toList());

		} else {
			throw new EntityNotFoundException("Business Entity not exists!");
		}

	}
	
	
	
	

}
