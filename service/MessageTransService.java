package com.record.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.record.dto.MessageTransDto;
import com.record.exception.ResourceNotFoundException;
import com.record.model.Layer;
import com.record.model.MessageTrans;
import com.record.model.PPW;
import com.record.model.Risk;
import com.record.repository.LayerRepository;
import com.record.repository.MessageTransRepository;
import com.record.repository.RiskRepository;
import com.record.util.MapperUtil;

@Service
public class MessageTransService {

	 private static final Logger LOGGER = LoggerFactory.getLogger(MessageTransService.class);
	 
    @Autowired
    private MessageTransRepository messageTransRepository;

    @Autowired
    private RiskRepository riskRepository;

    @Autowired
    private LayerRepository layerRepository;
    
    @Autowired
    private PpwService ppwService;

    public MessageTrans saveInboxMessage(MessageTrans messageTrans) {
        messageTrans.setMessageType("INCOMING");
        messageTrans.setMessageStatus("UNREAD");
        return messageTransRepository.save(messageTrans);
    }

    public MessageTrans updateMessage(MessageTrans updatedMessageTrans) {
        return messageTransRepository.save(updatedMessageTrans);
    }

    public MessageTrans getMessage(Long messageId) {
        return messageTransRepository.findById(messageId).orElseThrow(() -> {
            LOGGER.error("Error! message not found with id : " + messageId);
            return new ResourceNotFoundException("Error! Requested message not found");
        });
    }
    
    public MessageTrans getMessageByLayerId(Long layerId) {
        return messageTransRepository.findByLayerId(layerId).get();
    }

    public void updateInboxStatus(String id, String status) {
        MessageTrans messageTrans = messageTransRepository.findByOriginMessageId(id).get();
        messageTrans.setMessageStatus(status);
        messageTransRepository.save(messageTrans);
    }

    public List<MessageTrans> getUnreadMessage() {
        return messageTransRepository.findUnreadRows();
       // messageTrans.get(0).getMessageStatus();
        //return messageTransRepository.findTop1ByMessageStatusAndSenderEntityIdOrderByMessageDatetimeDesc("UNREAD", entityId);
    }

    public void saveMessageData(Risk risk, Layer layer, List<PPW> ppw) {
        riskRepository.save(risk);
        layer.setRisk(risk);
        
        ppw.stream().forEach(ppwObj->{
        	ppwObj.setLayer(layer);
        });
        layer.setPpws(ppw);
        layerRepository.save(layer);
        ppwService.savePpw(ppw, layer);
    }

    public MessageTrans save(MessageTrans messageTrans) {
        return messageTransRepository.save(messageTrans);
    }

    public MessageTransDto getMessageUsingOriginMessageId(String OriginMsgid)
  	{
  		Optional<MessageTrans> msg = messageTransRepository.findByOriginMessageId(OriginMsgid);
  		if (msg.isPresent()) {
  			System.out.println("**********************");
  			System.out.println(msg.toString());
  			System.out.println("**********************");
  			return MapperUtil.convertModelToEntity(msg.get(), MessageTransDto.class);
  		} else {
  			throw new EntityNotFoundException("Message " + OriginMsgid + "not found !");
  		}
  	}


}
