package com.record.mapper;

import org.springframework.stereotype.Component;

import com.record.entity.CalculationEntity;
import com.record.model.CalculationModel;

@Component
public class CalculationMapper {

	public CalculationModel mapToModel(CalculationEntity calculationEntity) {
		
		CalculationModel model = new CalculationModel();
		
		model.setCalculationCode(calculationEntity.getCalculationCode());
		model.setField1Name(calculationEntity.getField1Name());
		model.setField1Type(calculationEntity.getField1Type());
		model.setField2Name(calculationEntity.getField2Name());
		model.setField2Type(calculationEntity.getField2Type());
		model.setField3Name(calculationEntity.getField3Name());
		model.setField3Type(calculationEntity.getField3Type());
		model.setField4Name(calculationEntity.getField4Name());
		model.setField4Type(calculationEntity.getField4Type());
		model.setField5Name(calculationEntity.getField5Name());
		model.setField5Type(calculationEntity.getField5Type());
		model.setOperation(calculationEntity.getOperation());
		model.setTaxName(calculationEntity.getTaxName());
		
		return model;
		
	}
	
}
