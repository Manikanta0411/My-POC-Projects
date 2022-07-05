package com.record.mapper;

import static com.record.constant.LastOpIndicator.I;
import static com.record.mapper.LayerMapper.STANDARD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.record.dto.ParamFieldDto;
import com.record.entity.BusinessEntityOp;
import com.record.model.ParamField;
import com.record.service.BusinessEntityService;

@Component
public class ParamFieldMapper {

    @Autowired
    private BusinessEntityMapper businessEntityMapper;

    @Autowired
    private BusinessEntityService businessEntityService;

    public ParamFieldDto mapToDto(ParamField paramField) {
        ParamFieldDto dto = new ParamFieldDto();
        dto.setId(paramField.getId());
        dto.setFieldType(paramField.getFieldType());
        dto.setFieldKey(paramField.getFieldKey());
        dto.setDataType(paramField.getDataType());
        dto.setFieldValue(paramField.getFieldValue());
        dto.setOriginId(paramField.getOriginId());
        
//        List<Long> entityIds = paramField.getBusinessEntities().stream()
//                .map(entity -> entity.getId()).collect(Collectors.toList());
//        dto.setBusinessEntityIds(entityIds);

   //     List<BusinessEntityDto> businessEntityDtos = businessEntityMapper.mapToDtos(paramField.getBusinessEntities());
   //     dto.setBusinessEntityDtos(businessEntityDtos);
        return dto;
    }

    public List<ParamFieldDto> mapToDtos(Collection<ParamField> fields) {
        List<ParamFieldDto> dtos = new ArrayList<>();
        fields.forEach((field) -> {
            dtos.add(mapToDto(field));
        });
        return dtos;
    }

    public ParamField mapToModel(ParamFieldDto fieldDto) {
        ParamField paramField = new ParamField();
        paramField.setFieldType(STANDARD);
        paramField.setFieldKey(fieldDto.getFieldKey());
        paramField.setFieldValue(fieldDto.getFieldValue());
        paramField.setDataType(fieldDto.getDataType());
        paramField.setFieldType(fieldDto.getFieldType());
        paramField.setBusinessEntityIds(fieldDto.getBusinessEntityIds());
        paramField.setOriginId(fieldDto.getOriginId());
        paramField.setFieldCode(fieldDto.getFieldCode());
        paramField.setOprationType(fieldDto.getOprationType());
        

        //Collection<BusinessEntity> businessEntities = businessEntityMapper.mapToModels(fieldDto.getBusinessEntityDtos());
        if(fieldDto.getBusinessEntityIds() != null) {
            List<BusinessEntityOp> businessEntities = businessEntityService.getBusinessEntities(fieldDto.getBusinessEntityIds());
            businessEntities.forEach(entity -> {
               // entity.getParamFields().add(paramField);
            });
            paramField.setBusinessEntities(businessEntities);
        }

        if(fieldDto.getId() == null) {
            paramField.setCreatedDate(new Date());
            paramField.setLastOpIndicator(I);
        } else {
            paramField.setId(fieldDto.getId());
        }
        paramField.setUpdatedDate(new Date());
        return paramField;
    }
}
