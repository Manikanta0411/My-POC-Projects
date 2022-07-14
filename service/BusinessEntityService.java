package com.record.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.record.entity.BusinessEntityOp;
import com.record.repository.BusinessEntityRepository;

@Service
public class BusinessEntityService {

    @Autowired
    private BusinessEntityRepository businessEntityRepository;

    public List<BusinessEntityOp> getAllBusinessEntities() {
        //TODO: return Shortname + CountryCode (2 Char) + LOB CODE
        return businessEntityRepository.findAll();
    }

    public List<BusinessEntityOp> getBusinessEntities(Collection<Long> businessEntityIds) {
        return businessEntityRepository.findByIdIn(new HashSet<>(businessEntityIds));
    }
}
