package com.record.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.record.entity.BusinessEntityOp;
import com.record.entity.TraversalEntity;
import com.record.model.Layer;
import com.record.repository.TraversalRepository;

@Service
public class TraversalService {
	
	@Autowired
	private SystemParameterService parameterService;
	
	@Autowired
	private TraversalRepository traversalRepository;
	
    @Value("${sender.id}")
    private String senderId;
	
	public void createTraversal(Layer layer,BusinessEntityOp entity)
	{
		
		TraversalEntity newTraversal=new TraversalEntity();
		newTraversal.setGenratedId(senderId+parameterService.getTraversalSequence());//
		newTraversal.setSender(senderId);
		newTraversal.setReciver(entity.getId().toString());
		newTraversal.setLayerId(layer.getId());
	//	newTraversal.setRequestForwardId();
		newTraversal.setOpType("FORWARD");
		traversalRepository.save(newTraversal);
	}
}
