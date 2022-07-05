package com.record.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.record.dto.BusinessEntityDto;
import com.record.entity.BusinessEntityOp;
import com.record.model.BusinessEntity;

@Component
public class BusinessEntityMapper {

    public BusinessEntityDto mapToDto(BusinessEntityOp businessEntity) {
        BusinessEntityDto dto = new BusinessEntityDto();
        dto.setId(businessEntity.getId());
        dto.setShortName(businessEntity.getEntityShortName());
        //dto.setLobId(businessEntity.getl);
        //dto.setEmailId(businessEntity.gete);
        //dto.setType(businessEntity.getType());
        dto.setStatus(businessEntity.getStatus());
        return dto;
    }

    public List<BusinessEntityDto> mapToDtos(Collection<BusinessEntityOp> businessEntities) {
        List<BusinessEntityDto> businessEntityDtos = new ArrayList<>();
        businessEntities.forEach(entity -> {
            businessEntityDtos.add(mapToDto(entity));
        });
        return businessEntityDtos;
    }

    public BusinessEntity mapToModel(BusinessEntityDto businessEntityDto) {
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.setId(businessEntityDto.getId());
        businessEntity.setEmailId(businessEntityDto.getEmailId());
        businessEntity.setShortName(businessEntityDto.getShortName());
        businessEntity.setType(businessEntityDto.getType());
        businessEntity.setId(businessEntityDto.getId());
        return businessEntity;
    }

    public List<BusinessEntity> mapToModels(List<BusinessEntityDto> businessEntityDtos) {
        List<BusinessEntity> businessEntity = new ArrayList<>();
        businessEntityDtos.forEach(businessEntityDto -> {
            businessEntity.add(mapToModel(businessEntityDto));
        });
        return businessEntity;
    }
}
