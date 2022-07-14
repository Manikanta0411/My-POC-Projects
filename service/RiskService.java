package com.record.service;

import static com.record.constant.LastOpIndicator.I;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

//import jdk.nashorn.internal.runtime.options.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.record.exception.ResourceNotFoundException;
import com.record.model.Risk;
import com.record.repository.RiskRepository;

@Service
public class RiskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RiskService.class);

    @Autowired
    private RiskRepository riskRepository;

    @Transactional
    public Risk createRisk(Risk risk) {
        boolean isIdPresent = risk.getId() != null;
        risk.setLastOpIndicator(I);
        risk.setCreatedDate(new Date());
        risk.setUpdatedDate(new Date());
        risk.setOriginId(UUID.randomUUID().toString());
        Risk newRisk = riskRepository.save(risk);
        return newRisk;
    }

    public Optional<Risk> findByOriginId(String originId){
        return riskRepository.findByOriginId(originId);
    }

    public Risk getRisk(Long riskId) {
        return riskRepository.findById(riskId).orElseThrow(() -> {
            LOGGER.error("Error Risk detail not found for the id : " + riskId);
            return new ResourceNotFoundException("Error! Risk details not found");
        });
    }
}
