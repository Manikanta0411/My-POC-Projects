                                                                                 package com.record.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.record.entity.BusinessEntityOp;
import com.record.mapper.LayerMapper;
import com.record.mapper.PpwMapper;
import com.record.mapper.RiskMapper;
import com.record.model.MessageTrans;
import com.record.model.NotifyModel;
import com.record.repository.BusinessEntityRepository;
import com.record.repository.MessageTransRepository;

@Component
public class SynchupSchedulerNotify {

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
    private NotifyReciverService notifyReciverService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private BusinessEntityRepository businessEntityRepository;
    
    @Autowired
    private MessageTransRepository messageTransRepository;
    
    @Value("${server.port}")
    private String port;
    
	private static final Logger LOGGER = LoggerFactory.getLogger(SynchupSchedulerAttachment.class);

	 
	@Scheduled(fixedRate = 10000)
    public void readNotification() {
		
		LOGGER.debug("event = Process data for Notification Start : ");
		notifyReciverService.getAllUnReadMessage().stream().forEach(msg -> {

			LOGGER.debug("event = Processing Notification for data : "+msg.toString());
			Optional<BusinessEntityOp> entity = businessEntityRepository.findByEntityId(msg.getEntityId());
			if (entity.isPresent()) {
				if (readMessageFromSender(entity.get(), msg) != null) {
					StringBuilder URL = new StringBuilder();
					URL.append(entity.get().getEntityHostName()).append("notify/").append(msg.getOriginMessageId()).append("/true");
				    LOGGER.debug("event = Update Status of notification at Sender End : "+URL);
					restTemplate.put(URL.toString(), HttpMethod.PUT);
					msg.setIsRead(true);
					notifyReciverService.save(msg);
				}
			}else
			{
				LOGGER.debug("event = Business Entity Not Found! : "+msg.getEntityId());

			}

		});
    
    }
    
    
    public MessageTrans readMessageFromSender(BusinessEntityOp entity,NotifyModel msg)
	{

		StringBuilder URL = new StringBuilder();
		URL.append(entity.getEntityHostName()).append("readMsg/").append(msg.getOriginMessageId());
		LOGGER.debug("event = Reading Data for Message on URL : " + URL);
		LOGGER.debug("event = Reading Data for Message  : " + msg.toString());
		MessageTrans mess = restTemplate.getForObject(URL.toString(), MessageTrans.class);
		mess.setIsOwner(false);
		if (mess != null) {
			LOGGER.debug("event = Data Reviced from the Sender  : " + mess.toString());
			MessageTrans msgSaved = messageTransService.save(mess);
			return msgSaved;
		} 
		return null;
	}

   
  
}
