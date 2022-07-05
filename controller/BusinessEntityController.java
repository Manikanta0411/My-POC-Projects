package com.record.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.record.dto.BusinessEntityDto;
import com.record.entity.BusinessEntityOp;
import com.record.mapper.BusinessEntityMapper;
import com.record.service.BusinessEntityService;

@RestController
public class BusinessEntityController {

    @Autowired
    private BusinessEntityService businessEntityService;

    @Autowired
    private BusinessEntityMapper businessEntityMapper;

    @GetMapping("/businessEntity")
    private ResponseEntity<List<BusinessEntityDto>> getBusinessEntities() {
        List<BusinessEntityOp> allBusinessEntities = businessEntityService.getAllBusinessEntities();
        List<BusinessEntityDto> businessEntityDtos = businessEntityMapper.mapToDtos(allBusinessEntities);
        return ResponseEntity.ok(businessEntityDtos);
    }
}
