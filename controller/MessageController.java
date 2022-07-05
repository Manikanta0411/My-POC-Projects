package com.record.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.record.dto.*;
import com.record.exception.BadRequestException;
import com.record.mapper.LayerMapper;
import com.record.mapper.MessageTransMapper;
import com.record.mapper.PpwMapper;
import com.record.mapper.RiskMapper;
import com.record.model.Layer;
import com.record.model.MessageTrans;
import com.record.model.PPW;
import com.record.model.Risk;
import com.record.service.MessageTransService;

import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.record.util.Constants.RECORD_ADMIN_USER_TOKEN;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class MessageController {

    @Autowired
    private MessageTransService messageTransService;

    @Autowired
    private MessageTransMapper messageTransMapper;

    @Autowired
    private RiskMapper riskMapper;

    @Autowired
    private LayerMapper layerMapper;

    @Autowired
    private PpwMapper ppwMapper;

    
    
    @PostMapping("/message")
    public ResponseEntity<MessageTrans> addInboxMessage(@RequestBody MessageTransDto messageTransDto,
    		@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) throws URISyntaxException {
        MessageTrans messageTrans = messageTransService.saveInboxMessage(messageTransMapper.mapToDomain(messageTransDto));
        messageTrans.setMessageStatus("UNREAD");
        return ResponseEntity.created(new URI("/message/" + messageTrans.getId())).body(messageTrans);
    }

    /*@PostMapping("/message/{messageId}/{entityId}")
    public ResponseEntity<MessageTrans> saveMessageData(@PathVariable(name = "messageId") Long messageId,
                                                   @PathVariable(name = "entityId") Long entityId) throws URISyntaxException {
        MessageTrans latestMessage = messageTransService.getLatestMessage(entityId);

        try {
            EntityMessageDto entityMessageDto = new ObjectMapper().readValue(latestMessage.getMessage(), EntityMessageDto.class);
            RiskDto riskDto = entityMessageDto.getRiskDto();
            riskDto.setId(null);
            Risk risk = riskMapper.mapToModel(riskDto);
            LayerDto layerDto = entityMessageDto.getLayerDto();
            List<ParamFieldDto> paramFields = entityMessageDto.getParamFields();
            for (ParamFieldDto paramField : paramFields) {
                paramField.setId(null);
            }
            layerDto.setParamFieldDtos(paramFields);
            Layer layer = layerMapper.mapToModel(layerDto);
            PpwDto ppwDto = entityMessageDto.getPpwDto();

            PPW ppw = ppwMapper.mapToModel(ppwDto);

            messageTransService.saveMessageData(risk, layer, ppw);

        } catch (JsonProcessingException e) {
            //TODO: log message
        }
        return ResponseEntity.ok().build();
    }
*/
    @PutMapping("/message/{messageId}")
    public ResponseEntity<MessageTrans> updateMessage(@RequestBody MessageTransDto messageTransDto,
    		@PathVariable(name = "messageId") Long messageId,
    		@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) throws URISyntaxException {
        MessageTrans message = messageTransService.getMessage(messageId);
        message.setMessage(messageTransDto.getMessage());
        message.setId(messageId);
        messageTransService.updateMessage(message);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/message/{messageId}/{status}")
    public ResponseEntity<MessageTrans> updateMessageStatus(@PathVariable(name = "messageId") String messageId,
    		@PathVariable(name = "status") String status,
    		@ApiParam(value = "User Token issued during login call") @RequestHeader(value = RECORD_ADMIN_USER_TOKEN) String UserToken) throws URISyntaxException {
        messageTransService.updateInboxStatus(messageId, status);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/readMsg/{OriginMessageId}")
	public ResponseEntity<MessageTransDto> getMessage(@PathVariable("OriginMessageId") String msgId)
			throws BadRequestException {
		return new ResponseEntity<>(messageTransService.getMessageUsingOriginMessageId(msgId), HttpStatus.OK);
	}

}
