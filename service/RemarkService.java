package com.record.service;

import java.util.Date;
import java.util.Objects;
<<<<<<< HEAD
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.record.constant.LastOpIndicator;
import com.record.entity.HeadingEntity;
import com.record.entity.Remarks;
import com.record.exception.BadRequestException;
import com.record.model.Layer;
import com.record.model.RemarksModel;
import com.record.repository.HeadingRepository;
import com.record.repository.LayerRepository;
import com.record.repository.RemarkRepository;
import com.record.util.MapperUtil;

@Service
public class RemarkService {

	@Autowired
	private RemarkRepository remarkRepository;

	@Autowired
	private SystemParameterService parameterService;
	
	@Autowired
	private LayerService layerService;
	
	@Autowired
	private LayerRepository layerRepository;
	
	@Autowired
	private HeadingRepository headingRepository;

	public RemarksModel createRemarks(RemarksModel remarksModel) throws BadRequestException {

		Remarks remarks = MapperUtil.convertModelToEntity(remarksModel, Remarks.class);
		Optional<Layer> layer = layerRepository.findById(remarksModel.getLayerId());
		Optional<HeadingEntity> headingEntity = headingRepository.findById(remarksModel.getHeadingId());
		if(layer.isPresent() && headingEntity.isPresent()) {
			remarks.setLastOpIndicator(LastOpIndicator.C);
			remarks.setCreatedDate(new Date());
			remarks.setUpdatedDate(new Date());
			remarks.setStatus(true);
			remarks.setRemarksCode("REM_" + parameterService.getRemarksSequence());
			Remarks remarksEntity = remarkRepository.save(remarks);
			return MapperUtil.convertEntityToModel(remarksEntity, RemarksModel.class);
		}else {
			if(!layer.isPresent()) {
				throw new EntityNotFoundException("layer with id " + remarksModel.getLayerId() + " not found");
			}
			if(!headingEntity.isPresent()) {
				throw new EntityNotFoundException("heading with id " + remarksModel.getHeadingId() + " not found");
			}
			return null;
		}
		
	}

	public Remarks findById(Long remarkId) throws EntityNotFoundException {
		Remarks remarks = remarkRepository.findById(remarkId).orElse(null);
		if (Objects.isNull(remarks)) {
			throw new EntityNotFoundException("Remarks with id " + remarkId + " not found");
		}
		return remarks;
	}

	public Remarks findByCode(String remarksCode) throws EntityNotFoundException {
		Remarks remarks = remarkRepository.findByRemarksCode(remarksCode).orElse(null);
		if (Objects.isNull(remarks)) {
			throw new EntityNotFoundException("Remarks with id " + remarksCode + " not found");
		}
		return remarks;
	}

	public RemarksModel findByRemarkId(Long remarkId) {
		return MapperUtil.convertEntityToModel(findById(remarkId), RemarksModel.class);
	}

	public RemarksModel findByRemarkCode(String remarksCode) {
		return MapperUtil.convertEntityToModel(findByCode(remarksCode), RemarksModel.class);
	}

	public RemarksModel updateRemarks(Long remarkId, RemarksModel remarksModel) throws EntityNotFoundException {
		Remarks remarks = findById(remarkId);
		remarks.setRemarks(remarksModel.getRemarks());
		remarks.setLastOpIndicator(LastOpIndicator.U);
		remarks.setCreatedDate(new Date());
		remarks.setUpdatedDate(new Date());
		Remarks remarksEntity = remarkRepository.save(remarks);
		return MapperUtil.convertEntityToModel(remarksEntity, RemarksModel.class);
	}

	public RemarksModel updateRemarksByCode(String remarkCode, RemarksModel remarksModel)
			throws EntityNotFoundException {
		Remarks remarks = findByCode(remarkCode);
		remarks.setRemarks(remarksModel.getRemarks());
		remarks.setLastOpIndicator(LastOpIndicator.U);
		remarks.setCreatedDate(new Date());
		remarks.setUpdatedDate(new Date());
		Remarks remarksEntity = remarkRepository.save(remarks);
		return MapperUtil.convertEntityToModel(remarksEntity, RemarksModel.class);
	}

	public void deleteRemarks(Long remarkId) {
		Remarks remarks = findById(remarkId);
		if (remarks != null) {
			remarks.setLastOpIndicator(LastOpIndicator.U);
			remarks.setStatus(false);
		}
	}

	public void deleteRemarksByCode(String remarkCode) {
		Remarks remarks = findByCode(remarkCode);
		if (remarks != null) {
			remarks.setLastOpIndicator(LastOpIndicator.U);
			remarks.setStatus(false);
=======

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.record.constant.LastOpIndicator;
import com.record.entity.Remarks;
import com.record.exception.BadRequestException;
import com.record.model.RemarksModel;
import com.record.repository.RemarkRepository;
import com.record.util.MapperUtil;

@Service
public class RemarkService {

	@Autowired
	private RemarkRepository remarkRepository;
	
	public RemarksModel createRemarks(RemarksModel remarksModel) throws BadRequestException {

		Remarks remarks = MapperUtil.convertModelToEntity(remarksModel, Remarks.class);
		
		remarks.setLastOpIndicator(LastOpIndicator.C);
		remarks.setCreatedDate(new Date());
		remarks.setUpdatedDate(new Date());
		Remarks remarksEntity = remarkRepository.save(remarks);
		return MapperUtil.convertEntityToModel(remarksEntity, RemarksModel.class);
	}
	
	public Remarks findById(Long remarkId) throws EntityNotFoundException  {
		Remarks remarks = remarkRepository.findById(remarkId).orElse(null);
		if (Objects.isNull(remarks)) {
			throw new EntityNotFoundException("Remarks with id " + remarkId + " not found");
		}
		return remarks;
	}
	
	public RemarksModel findByRemarkId(Long remarkId) {
		return MapperUtil.convertEntityToModel(findById(remarkId), RemarksModel.class);
	}
	
	
	public RemarksModel updateRemarks(Long remarkId,RemarksModel remarksModel) throws EntityNotFoundException{
		Remarks remarks = findById(remarkId);
		remarks.setRemarks(remarksModel.getRemarks());
		remarks.setLastOpIndicator(LastOpIndicator.U);
		remarks.setCreatedDate(new Date());
		remarks.setUpdatedDate(new Date());
		Remarks remarksEntity = remarkRepository.save(remarks);
		return MapperUtil.convertEntityToModel(remarksEntity, RemarksModel.class);
	}
	
	
	public void deleteRemarks(Long remarkId) {
		Remarks remarks = findById(remarkId);
		if(remarks != null) {
			remarkRepository.deleteById(remarkId);
>>>>>>> branch 'Mani_work_June8th2022' of https://github.com/RE-cord/Record_Backend_API.git
		}
	}

}
