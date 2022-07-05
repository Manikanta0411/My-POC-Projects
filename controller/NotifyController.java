package com.record.controller;

import static com.record.util.Constants.NOTIFY;

import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.record.exception.BadRequestException;
import com.record.model.MessageTrans;
import com.record.model.NotifyModel;
import com.record.service.NotifyReciverService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(NOTIFY)
@Api(value = "Action Controller", tags = { "notify" })
public class NotifyController {
	
	@Autowired
	public NotifyReciverService notifyReciverService; 	
	@PostMapping
	public ResponseEntity<NotifyModel> createNotify(@Valid @RequestBody NotifyModel notifyReq)
			throws BadRequestException {
		return new ResponseEntity<>(notifyReciverService.save(notifyReq), HttpStatus.CREATED);
	}
	
    @PutMapping("/{messageId}/{status}")
    public ResponseEntity<MessageTrans> updateMessageStatus(@PathVariable(name = "messageId") String messageId, @PathVariable(name = "status") String status) throws URISyntaxException {
    	notifyReciverService.updateInboxStatus(messageId, status);
        return ResponseEntity.ok().build();
    }
	
	
	

}
