package com.record.service;

import com.record.model.SystemParameter;
import com.record.repository.SystemParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemParameterService {

    @Autowired
    private SystemParameterRepository parameterRepository;

    public Integer getRiskSequence() {
        SystemParameter riskSequence = parameterRepository.findByCode("RISK_SEQ");
        Integer seq = Integer.parseInt(riskSequence.getValue());
        Integer nextSeq = seq + 1;
        riskSequence.setValue(nextSeq.toString());
        parameterRepository.save(riskSequence);
        return nextSeq;
    }
    
    
    public Integer getTraversalSequence() {
        SystemParameter riskSequence = parameterRepository.findByCode("CEDANT_SEQ");//todo : depends on who is using the app this code will be used
        Integer seq = Integer.parseInt(riskSequence.getValue());
        Integer nextSeq = seq + 1;
        riskSequence.setValue(nextSeq.toString());
        parameterRepository.save(riskSequence);
        return nextSeq;
    }
    
    
    public Integer getParamFieldSequence() {
        SystemParameter riskSequence = parameterRepository.findByCode("PARAM_SEQ");//todo : depends on who is using the app this code will be used
        Integer seq = Integer.parseInt(riskSequence.getValue());
        Integer nextSeq = seq + 1;
        riskSequence.setValue(nextSeq.toString());
        parameterRepository.save(riskSequence);
        return nextSeq;
    }
    
    
    public Integer getPpwSequence() {
        SystemParameter riskSequence = parameterRepository.findByCode("PPW_SEQ");//todo : depends on who is using the app this code will be used
        Integer seq = Integer.parseInt(riskSequence.getValue());
        Integer nextSeq = seq + 1;
        riskSequence.setValue(nextSeq.toString());
        parameterRepository.save(riskSequence);
        return nextSeq;
    }
    
    public Integer getCalcSequence() {
        SystemParameter calcSequence = parameterRepository.findByCode("CCD_SEQ");//todo : depends on who is using the app this code will be used
        Integer seq = Integer.parseInt(calcSequence.getValue());
        Integer nextSeq = seq + 1;
        calcSequence.setValue(nextSeq.toString());
        parameterRepository.save(calcSequence);
        return nextSeq;
    }
    
    public Integer getTaxSequence() {
        SystemParameter taxSequence = parameterRepository.findByCode("TAX_SEQ");//todo : depends on who is using the app this code will be used
        Integer seq = Integer.parseInt(taxSequence.getValue());
        Integer nextSeq = seq + 1;
        taxSequence.setValue(nextSeq.toString());
        parameterRepository.save(taxSequence);
        return nextSeq;
    }
    
    public Integer getRepoSequence() {
        SystemParameter repoSequence = parameterRepository.findByCode("REPO_SEQ");//todo : depends on who is using the app this code will be used
        Integer seq = Integer.parseInt(repoSequence.getValue());
        Integer nextSeq = seq + 1;
        repoSequence.setValue(nextSeq.toString());
        parameterRepository.save(repoSequence);
        return nextSeq;
    }
    
    public Integer getRemarksSequence() {
        SystemParameter remarksSequence = parameterRepository.findByCode("REM_SEQ");//todo : depends on who is using the app this code will be used
        Integer seq = Integer.parseInt(remarksSequence.getValue());
        Integer nextSeq = seq + 1;
        remarksSequence.setValue(nextSeq.toString());
        parameterRepository.save(remarksSequence);
        return nextSeq;
    }
    
    public Integer getHeadingSequence() {
        SystemParameter headingSequence = parameterRepository.findByCode("HEAD_SEQ");//todo : depends on who is using the app this code will be used
        Integer seq = Integer.parseInt(headingSequence.getValue());
        Integer nextSeq = seq + 1;
        headingSequence.setValue(nextSeq.toString());
        parameterRepository.save(headingSequence);
        return nextSeq;
    }
}
