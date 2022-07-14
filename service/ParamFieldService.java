package com.record.service;

import com.record.model.ParamField;
import com.record.repository.ParamFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParamFieldService {

    @Autowired
    private ParamFieldRepository paramFieldRepository;

    public Optional<ParamField> findByOriginId(String originId) {
        return paramFieldRepository.findByOriginId(originId);
    }
}
