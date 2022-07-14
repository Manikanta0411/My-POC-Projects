package com.record.service;

import com.record.model.ReferenceValue;
import com.record.repository.ReferenceValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReferenceValueService {

    @Autowired
    private ReferenceValueRepository referenceValueRepository;

    public Map<String, String> getReferenceValue(String refModule, String refCategory) {
        List<ReferenceValue> referenceValues = referenceValueRepository.findByRefModuleAndRefCategory(refModule, refCategory);
        Map<String, String> refValMap = new HashMap<>();
        if(referenceValues != null) {
            referenceValues.forEach(refVal -> {
                refValMap.put(refVal.getId().getRefKey(), refVal.getRefValue());
            });
        }
        return  refValMap;
    }
}
