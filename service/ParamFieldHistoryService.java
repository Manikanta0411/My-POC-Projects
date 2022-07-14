package com.record.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.record.dto.BusinessEntityDto;
import com.record.entity.ParamEntityLink;
import com.record.model.Layer;
import com.record.model.ParamField;
import com.record.model.ParamFieldHistory;
import com.record.repository.ParamEntityLinkRepository;
import com.record.repository.ParamFieldHistoryRepository;
import com.record.repository.ParamFieldRepository;

@Service
public class ParamFieldHistoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParamFieldHistoryService.class);
	
    @Autowired
    private ParamFieldHistoryRepository paramFieldHistoryRepository;
    
    @Autowired
    private ParamFieldRepository paramFieldRepository; 
    
    @Autowired
    private ParamEntityLinkRepository entityLinkRepository;

    private void saveHistory() {

    }

	public void createHistoryRecord(Layer layer,List<BusinessEntityDto> reqEntities) {
		
		Map<String, ParamField> currentRecords = getCurrentRecords(layer);
		
		Map<String, ParamFieldHistory> previousRecords = getPreviousRecords(layer,reqEntities);
		currentRecords.forEach((key, paramField) -> {
			ParamFieldHistory paramFieldHistory = previousRecords.get(key);
			if (paramFieldHistory != null) {
				boolean equalValues = equalValues(paramField.getFieldValue(), paramFieldHistory.getParamValue());
				if (!equalValues) {
					createParamHistoryFieldRecord(paramField, paramFieldHistory.getId(),null);
				}
			}
		});
	}

    private boolean equalValues(String currentValue, String historyValue) {
        if (currentValue != null || historyValue != null) {
            if (currentValue == null && historyValue != null) {
                return false;
            }
            if (currentValue != null && historyValue == null) {
                return false;
            }
            return currentValue.equals(historyValue);
        }
        return true;
    }

    private Map<String, ParamField> getCurrentRecords(Layer layer) {
        Map<String, ParamField> currentValueMap = new HashMap<>();
        layer.getParamFields().forEach(paramField -> {
            currentValueMap.put(paramField.getFieldKey()+paramField.getFieldCode(), paramField);
        });
        return currentValueMap;
    }

	private Map<String, ParamFieldHistory> getPreviousRecords(Layer layer, List<BusinessEntityDto> reqEntities) {
		Map<String, ParamFieldHistory> paramFieldMap = new HashMap<>();
		layer.getParamFields().forEach(paramField -> {
			ParamFieldHistory fieldHistory = paramFieldHistoryRepository
					.findByParamFieldIdAndLayerId(paramField.getId());
			if (fieldHistory != null) {
				paramFieldMap.put(fieldHistory.getFieldKey() + fieldHistory.getFieldCode(), fieldHistory);
			    return;
			} else if
				 (paramField.getClonedOriginId() != null) {
					reqEntities.stream().forEach(rEntity -> {
						LOGGER.debug("Check for ParamEntityLink Id " + "ParamField Id : " + paramField.getId()
								+ " EntityId : " + rEntity);
						
						Optional<ParamEntityLink> paramEntityLinkResult = entityLinkRepository
								.findByLayerAndBusinessEntityAndRiskAndStatusAndParamFieldId(paramField.getId(),
										paramField.getLayer().getId(), rEntity.getId(),
										paramField.getLayer().getRisk().getId(), "ACTIVE");
						
						
						
						if (paramEntityLinkResult.isPresent()) {
							LOGGER.debug("Create History for Cloned Field " + "ParamField Id : " + paramField.getId()
									+ " EntityId : " + rEntity);
							
							LOGGER.debug("ParamEntityLink Id for the cloned filed is : "
									+ paramEntityLinkResult.get().getId());
							
							
							Optional<ParamField> orignalParamFiled = paramFieldRepository
									.getIdByOriginId(paramField.getClonedOriginId()); // param field id 3
							
							if (orignalParamFiled.isPresent()) {
								ParamFieldHistory clonedParamFiledHistoryRecord = paramFieldHistoryRepository
										.getIdByMatchingHistoryIdAndPreRecordId(orignalParamFiled.get().getId());

								createParamHistoryFieldRecord(paramField, clonedParamFiledHistoryRecord.getId(),
										paramEntityLinkResult.get().getId());

							}
						}

					});
				} else {
					createParamHistoryFieldRecord(paramField, null, null);

				}
			
		});
		return paramFieldMap;
	}

    private void createParamHistoryFieldRecord(ParamField paramField, Long previousId,Long paramEntityLinkId) {
        ParamFieldHistory paramFieldHistory = new ParamFieldHistory();
        paramFieldHistory.setParamFieldId(paramField.getId());
        paramFieldHistory.setParamValue(paramField.getFieldValue());
        paramFieldHistory.setFieldKey(paramField.getFieldKey());
        paramFieldHistory.setLayerId(paramField.getLayer().getId());
        paramFieldHistory.setRiskId(paramField.getLayer().getRisk().getId());
        paramFieldHistory.setDataType(paramField.getDataType());
        paramFieldHistory.setEntityId(100000L);
        paramFieldHistory.setFieldCode(paramField.getFieldCode());
       // paramFieldHistory.setCreatedBy("currentUser");
        paramFieldHistory.setUpdatedDate(new Date());
        paramFieldHistory.setCreatedDate(new Date());
        if(paramEntityLinkId!=null)
        { 	
        paramFieldHistory.setParamEntityLinkId(paramEntityLinkId);
        }
        ParamFieldHistory paramFieldHist = paramFieldHistoryRepository.save(paramFieldHistory);
        if (previousId != null) {
            paramFieldHist.setPreviousRecordId(previousId);
        } else {
            paramFieldHist.setPreviousRecordId(paramFieldHist.getId());
        }
        paramFieldHistoryRepository.save(paramFieldHist);
    }
}
