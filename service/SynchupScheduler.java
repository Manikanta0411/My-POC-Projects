                                                                                 package com.record.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.record.dto.*;
import com.record.mapper.LayerMapper;
import com.record.mapper.PpwMapper;
import com.record.mapper.RiskMapper;
import com.record.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import static com.record.constant.LastOpIndicator.I;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class SynchupScheduler {

    @Autowired
    private MessageTransService messageTransService;

    @Autowired
    private RiskMapper riskMapper;

    @Autowired
    private LayerMapper layerMapper;

    @Autowired
    private RiskService riskService;

    @Autowired
    private LayerService layerService;

    @Autowired
    private PpwMapper ppwMapper;

    @Autowired
    private PpwService ppwService;

    @Autowired
    private ParamFieldService paramFieldService;

    @Autowired
    private AttachmentTransService attachmentTransService;
    
    @Autowired
    private SystemParameterService parameterService;
    

    @Scheduled(fixedRate = 10000)
    public void synchronizeMessages() {
        List<MessageTrans> unreadMessage = messageTransService.getUnreadMessage();
        //MessageTrans latestMessage = messageTransService.getLatestMessage(entityId);

        unreadMessage.forEach(messageTrans -> {
            try {
                EntityMessageDto entityMessageDto = new ObjectMapper().readValue(messageTrans.getMessage(), EntityMessageDto.class);

                RiskDto riskDto = entityMessageDto.getRiskDto();
                riskDto.setRiskId(null); //setting null so that new risk id will be genrated on reciver side
                Risk risk = getRisk(riskDto);
                Layer layer = getLayer(entityMessageDto);
                List<PPW> ppw = getPpw(entityMessageDto);

                messageTransService.saveMessageData(risk, layer, ppw);
                messageTrans.setMessageStatus("READ");
                messageTransService.save(messageTrans);
                if(entityMessageDto.getAttachmentIds()!=null)
                {
  				    attachmentTransService.saveAttachments(entityMessageDto.getAttachmentIds(),
  						  layer.getId(), entityMessageDto.getSenderEntityId(), "UNREAD");
	 
                }

            } catch (JsonProcessingException e) {
                //TODO: log message
            }
        });
    }

    private List<PPW> getPpw(EntityMessageDto entityMessageDto) {
    	
    	List<PPW> ppwList=new ArrayList<PPW>();
       // PpwDto ppwDto = entityMessageDto.getPpwDto().get(0);
    	entityMessageDto.getPpwDto().stream().forEach(ppwDto -> {
    	    PPW ppw = ppwMapper.mapToModel(ppwDto);
            Optional<PPW> ppwOptional = ppwService.findByOriginId(ppwDto.getOriginId());
            if(!ppwOptional.isPresent()) {
                ppw.setId(null);
            }else
            {
            	ppw.setId(ppwOptional.get().getId());
            }
            
            ppwList.add(ppw);
    	});
    	
        return ppwList;
    }

    private Layer getLayer(EntityMessageDto entityMessageDto) {
        LayerDto layerDto = entityMessageDto.getLayerDto();
        List<ParamFieldDto> paramFields = entityMessageDto.getParamFields();

        for (ParamFieldDto paramField : paramFields) {
            Optional<ParamField> paramFieldOptional = paramFieldService.findByOriginId(paramField.getOriginId());
            if(!paramFieldOptional.isPresent()) {
                paramField.setId(null);
            } else {
                paramField.setId(paramFieldOptional.get().getId());
            }
    		paramField.setFieldCode("P_"+parameterService.getParamFieldSequence());

        }
        layerDto.setParamFieldDtos(paramFields);

        Layer layer = layerMapper.mapToModel(layerDto);
        Optional<Layer> layerOptional = layerService.findByOriginId(layerDto.getOriginId());
        if (!layerOptional.isPresent()) {
            layer.setId(null);
        } else {
            layer.setId(layerOptional.get().getId());
        }
        return layer;
    }

    private Risk getRisk(RiskDto riskDto) {
        Optional<Risk> riskOptional = riskService.findByOriginId(riskDto.getOriginId());
        Risk risk = riskMapper.mapToModel(riskDto);
        if (!riskOptional.isPresent()) {
            risk.setId(null);
        } else {
            risk.setId(riskOptional.get().getId());
        }
        return risk;
    }
    
    
}
