package com.record.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.record.constant.LayerFieldEnum;
import com.record.dto.*;
import com.record.exception.LayerFieldValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class LayerValidator {

    public void validateLayerFields(LayerDto layerDto) {
        List<ParamFieldDto> paramFieldDtos = layerDto.getParamFieldDtos();
        List<FieldErrorDto> fieldErrorDtos = new ArrayList<>();
        paramFieldDtos.forEach(paramFieldDto -> {
            if ("ppw".equals(paramFieldDto.getFieldKey())) {
            	paramFieldDto.getPpwDto().stream().forEach(valueAsObject->{
                	validatePPW(valueAsObject, fieldErrorDtos);
            	});
            } else if ("STANDARD".equals(paramFieldDto.getFieldType())) {
                LayerFieldEnum fieldEnum = getEnum(paramFieldDto.getFieldKey());
                validateField(paramFieldDto.getFieldValue(), fieldEnum, fieldErrorDtos);
            }
        });
        if (!fieldErrorDtos.isEmpty()) {
            throw new LayerFieldValidationException(fieldErrorDtos);
        }
    }

    private void validatePPW(Object fieldValue, List<FieldErrorDto> fieldErrorDtos) {
        ObjectMapper mapper = new ObjectMapper();
        PpwDto ppw = mapper.convertValue(fieldValue, PpwDto.class);

        int numOfInstallment = ppw.getNumOfInstallment();
        String errorMessage = null;

        List<PremiumDto> premiumListDto = ppw.getPremiumListDto();
        if (premiumListDto.size() != numOfInstallment + 1) {
            errorMessage = "Number of installment should be : " + (premiumListDto.size() + 1);
		} else {
			BigDecimal sumOfInstallments = BigDecimal.ZERO;
			for (PremiumDto dto : premiumListDto) {
				sumOfInstallments = sumOfInstallments.add(dto.getInstallmentPercent());
			}
			if (sumOfInstallments.compareTo(new BigDecimal(100)) != 0) {
				errorMessage = "Sum of installments percent should be 100";
			}
		}
        if (errorMessage != null) {
            FieldErrorDto fieldErrorDto = new FieldErrorDto();
            fieldErrorDto.setFieldName("ppw");
            fieldErrorDto.setError(errorMessage);
            fieldErrorDtos.add(fieldErrorDto);
        }
    }

    private void validateField(String fieldValue, LayerFieldEnum fieldEnum, List<FieldErrorDto> fieldErrorDtos) {
        if (fieldEnum.isValidationRequired()) {
            if (fieldValue.length() > fieldEnum.getMaxSize()) {
                FieldErrorDto fieldErrorDto = new FieldErrorDto();
                fieldErrorDto.setValue(fieldValue);
                fieldErrorDto.setError("Max length of the field " + fieldEnum.getFieldName() + " is " + fieldEnum.getMaxSize());
                fieldErrorDto.setFieldName(fieldEnum.getFieldName());
                fieldErrorDtos.add(fieldErrorDto);
            }
        }
    }

    public LayerFieldEnum getEnum(String fieldName) {
        return LayerFieldEnum.stream().filter(fieldEnum -> fieldEnum.getFieldName().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Field not present with name : " + fieldName));
    }
}



