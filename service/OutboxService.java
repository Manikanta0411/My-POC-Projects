package com.record.service;

import static com.record.constant.RecordConstants.OUTGOING;
import static com.record.constant.RecordConstants.UNREAD;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.record.dto.BusinessEntityDto;
import com.record.dto.EntityMessageDto;
import com.record.dto.LayerDto;
import com.record.dto.ParamFieldDto;
import com.record.dto.PpwDto;
import com.record.dto.RiskDto;
import com.record.entity.BusinessEntityOp;
import com.record.entity.NotifyEntity;
import com.record.entity.ParamEntityLink;
import com.record.exception.RecordException;
import com.record.mapper.LayerMapper;
import com.record.mapper.ParamFieldMapper;
import com.record.mapper.PpwMapper;
import com.record.mapper.RiskMapper;
import com.record.model.Layer;
import com.record.model.MessageTrans;
import com.record.model.PPW;
import com.record.model.ParamField;
import com.record.repository.BusinessEntityRepository;
import com.record.repository.MessageTransRepository;
import com.record.repository.NotifyReceiverRepository;
import com.record.repository.ParamEntityLinkRepository;
import com.record.repository.ParamFieldRepository;

@Service
public class OutboxService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutboxService.class);

    @Autowired
    private MessageTransRepository messageTransRepository;

    @Autowired
    private ParamFieldMapper mapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MailService mailService;

    @Autowired
    private LayerMapper layerMapper;

    @Autowired
    private RiskMapper riskMapper;

    @Autowired
    private PpwMapper ppwMapper;
    
    @Autowired
    private NotifyReceiverRepository notifyReceiverRepository;

    @Autowired
    private TraversalService traversalService;
    
    @Autowired
    private ParamEntityLinkRepository paramEntityLinkRepository;
    
    @Autowired
    private ParamFieldRepository paramFieldRepository;
    
    @Autowired
    private BusinessEntityRepository businessEntityRepository;
    @Autowired
    private ParamFieldMapper paramFieldMapper;

    //This should be governed by user role
    @Value("${insurer.entity.id}")
    private String insurerId;

    
    
    public void saveOutboxNew(Layer layer,List<BusinessEntityDto> reqEntites)
	{
		reqEntites.stream().forEach(entity -> {
			Optional<BusinessEntityOp> businessEntity =businessEntityRepository.findById(entity.getId());
			if(businessEntity.isPresent()) {
				
			Optional<ParamEntityLink> paramEntityLink = paramEntityLinkRepository
					.findByLayerAndBusinessEntityAndRiskAndStatus(layer.getId(), entity.getId(), layer.getRisk().getId(),"ACTIVE");

			if (paramEntityLink.isPresent()) {

				LOGGER.debug("param_entity_link_tbl Id " + paramEntityLink.get().getId());

				ParamEntityLink currentParamEntityLink = paramEntityLink.get();

				List<String> paramFiledIdsList = Arrays.asList(currentParamEntityLink.getFieldIds().split(","));
				
				if (!paramFiledIdsList.isEmpty()) {
					List<ParamField> currentFields = new ArrayList<>();
					paramFiledIdsList.stream().forEach(paramFieldId -> {
						Optional<ParamField> savedParamFiled = paramFieldRepository
								.findById(Long.parseLong(paramFieldId));
						if (savedParamFiled.isPresent()) {
							currentFields.add(savedParamFiled.get());
						}
					});
					
					Optional<MessageTrans> previusMsgTransData= messageTransRepository.getLatestMsgFromMsgTransByRiskAndLayerandEntity(layer.getRisk().getId(),layer.getId(),entity.getId());

					if(previusMsgTransData.isPresent())
					{
						
						try {
							EntityMessageDto previousEntityMessageDto = new ObjectMapper().readValue(previusMsgTransData.get().getMessage(), EntityMessageDto.class);
							
						    List<ParamFieldDto> previousParamFieldsDto=previousEntityMessageDto.getParamFields();
						    
					        List<ParamField> previousParamFields = new ArrayList<>();
					        
					        previousParamFieldsDto.stream().forEach(paramFieldDto ->{
						    	ParamField paramField = paramFieldMapper.mapToModel(paramFieldDto);	
						    	previousParamFields.add(paramField);

						    });
							
						    currentFields.stream().forEach(field->{
						    	previousParamFields.stream().forEach(preParamField ->{
						    		if(field.getFieldKey().equalsIgnoreCase(preParamField.getFieldKey()))
						    		{
						    			field.setOriginId(preParamField.getOriginId());
						    		}
						    	});
						    });
						    
						    
						    
						} catch (JsonProcessingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	
					}

					String uuid = UUID.randomUUID().toString();
					EntityMessageDto entityMessageDto = entityMessageDto(layer, entity.getId(), currentFields);
					entityMessageDto.setPpwDto(getPpwDto(layer));
					
					String jsonData = getJsonData(entityMessageDto);
					MessageTrans outbox = getMessageTrans(layer, layer.getRisk().getId(), jsonData, entity.getId());
					outbox.setOriginMessageId(uuid);
					outbox.setMessageStatus("UNREAD");
					outbox.setIsOwner(true);
					
					
					messageTransRepository.save(outbox);  
					RestTemplate restTemplate = new RestTemplate();
		            StringBuilder URL=new StringBuilder();
		            URL.append(businessEntity.get().getEntityHostName()).append("notify");		
		           	
		            
		            try {
		  				 URI uri = new URI(URL.toString());
						 NotifyEntity notifyEntity=new NotifyEntity();
						 notifyEntity.setOriginMessageId(uuid);
						 //notifyEntity.setEntityId(businessEntity.get().getEntityId());
						 notifyEntity.setEntityId(insurerId.toString());
						 notifyEntity.setIsRead(false);
						 notifyEntity.setIsSentByMe(true);
						 notifyReceiverRepository.save(notifyEntity);
						 try{
							 System.out.println("******************************");
							 System.out.println(uri);
							 System.out.println("******************************");
							 ResponseEntity<String> result = restTemplate.postForEntity(uri, notifyEntity, String.class);
							 System.out.println(result.getStatusCodeValue());
						 } catch(ResourceAccessException exception){
						     exception.printStackTrace();
						} 
						 
						 
					} catch (URISyntaxException e) {
						e.printStackTrace();
						LOGGER.error("notificaiton send error");
					}
					
					

				}

			}
			}else
			{
				throw new EntityNotFoundException("Entity Not "+entity.getId()+" Found! ");
			}

		});

	}
    
    
    public void saveOutbox(Layer layer) {
        Set<BusinessEntityOp> businessEntities = new HashSet<>();
        List<ParamField> paramFields = layer.getParamFields();
       List<ParamEntityLink> paramEntityLink = paramEntityLinkRepository.findByLayer(layer);
       HashMap<Long, List<ParamField>> entityParamsMap = getEntityParamsMap(paramFields,paramEntityLink);
        paramFields.forEach(paramField -> {
            businessEntities.addAll(paramField.getBusinessEntities());
        });

        Long riskId = layer.getRisk().getId();
        //String userName = RecordSecurityContext.getUserName();
        String userName = "";

        businessEntities.forEach((entity) -> {
        	
        	String uuid= UUID.randomUUID().toString();
        	List<ParamField> fields = entityParamsMap.get(entity.getId());
            List<ParamField> filteredFields = fields.stream().filter(paramField -> !paramField.isSent()).collect(Collectors.toList());
            EntityMessageDto entityMessageDto = entityMessageDto(layer, entity.getId(), filteredFields);
            entityMessageDto.setPpwDto(getPpwDto(layer));
            String jsonData = getJsonData(entityMessageDto);
            MessageTrans outbox = getMessageTrans(layer, riskId, jsonData,entity.getId());
            outbox.setOriginMessageId(uuid);
            messageTransRepository.save(outbox);
            traversalService.createTraversal(layer,entity);
           // message.setOriginMessageId(uuid);

            /* ResponseEntity<EntityMessageDto> responseEntity = sendMessageToEntity(entityMessageDto, entity.getApiUrl());
            if (responseEntity == null) {
                outbox.setStatus("FAILED");
            }*/
            
            
            //mailService.sendEmail(layer, entity);
          //TODO:if the data is new or updated notify reciver
            
            RestTemplate restTemplate = new RestTemplate();
            StringBuilder URL=new StringBuilder();
            URL.append(entity.getEntityHostName()).append("/notify");		
           		
 //         final String baseUrl = "http://ec2-35-174-7-71.compute-1.amazonaws.com:"+9090+"/notify";
            
            try {
  				 URI uri = new URI(URL.toString());
				 NotifyEntity notifyEntity=new NotifyEntity();
				 notifyEntity.setOriginMessageId(uuid);
				 
				 System.out.println("<<<<<<<<<<<<<");
				 ResponseEntity<String> result = restTemplate.postForEntity(uri, notifyEntity, String.class);
				 System.out.println(">>>>>>>>>");
				 System.out.println(result.getStatusCodeValue());
				 
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error("notificaiton send error");
			}
			
        });
    }

    private MessageTrans getMessageTrans(Layer layer, Long riskId, String jsonData,Long entityId) {
        MessageTrans outbox = new MessageTrans();
        outbox.setRiskId(riskId);
        outbox.setLayerId(layer.getId());
        outbox.setSenderName(""); //TODO: Retrieve username from the session
        outbox.setMessageDatetime(new Date());
        outbox.setMessageType(OUTGOING);
        outbox.setSenderEntityId(insurerId); //TODO: Should be governed by user role 
        outbox.setMessage(jsonData);
        outbox.setMessageStatus(UNREAD);
        outbox.setOriginMessageId(UUID.randomUUID().toString());
        outbox.setReceiverEntityId(entityId);
        return outbox;
    }

    private HashMap<Long, List<ParamField>> getEntityParamsMap(List<ParamField> paramFields,List<ParamEntityLink> paramEntityLink ) {
        HashMap<Long, List<ParamField>> entityMessageMap = new HashMap<>();
        Set<BusinessEntityOp> businessEntity=new HashSet<BusinessEntityOp>();
        paramFields.stream().forEach(paramField -> {
        	paramEntityLink.stream().forEach(ee->{
        		 List<String> list = Arrays.asList(ee.getFieldIds().split(","));
        		 if (list.contains(paramField.getId().toString())) {
       			 businessEntity.add(ee.getBusinessEntity());
        		 }
        		
        	});
        	
//        	paramEntityLink.stream().forEach(ee->{
//        		 List<String> list = Arrays.asList(ee.getFieldIds().split(","));
//        		 if (list.contains(paramField.getId().toString())) {
//        			 businessEntity.add(ee.getBusinessEntity());
//        		 }
//        	});
        	
        	paramField.setBusinessEntities(businessEntity);
            paramField.getBusinessEntities().forEach(entity -> {
            	
                if (entityMessageMap.get(entity.getId()) == null) {
                    List paramSet = new ArrayList<>();
                    paramSet.add(paramField);
                    entityMessageMap.put(entity.getId(), paramSet);
                } else {
                    List<ParamField> paramSet = entityMessageMap.get(entity.getId());
                    paramSet.add(paramField);
                    entityMessageMap.put(entity.getId(), paramSet);
                }
            });
        });
        
        
        return entityMessageMap;
    }

    private List<PpwDto> getPpwDto(Layer layer) {
        List<PPW> ppws = layer.getPpws();
       // PPW ppw = ppws.get(0);
        List<PpwDto> ppwDtoList=new ArrayList<PpwDto>();
        ppws.stream().forEach(ppw ->{
        	 PpwDto ppwDto = ppwMapper.mapToDto(ppw);
        	 ppwDto.setBusinessEntityDtos(null);
        	 ppwDtoList.add(ppwDto);
        });
        return ppwDtoList;
    }

    private String getJsonData(EntityMessageDto entity) {
        return getJsonString(entity);
    }

    private EntityMessageDto entityMessageDto(Layer layer, Long entityId, List<ParamField> fields) {
        EntityMessageDto entityMessageDto = new EntityMessageDto();
        entityMessageDto.setLayerId(layer.getId());
        LayerDto layerDto = layerMapper.mapToDto(layer);
        layerDto.setParamFieldDtos(null);
        RiskDto riskDto = riskMapper.mapToDto(layer.getRisk());
        riskDto.setLayerDtos(null);
        entityMessageDto.setSenderEntityId(insurerId);
        entityMessageDto.setLayerDto(layerDto);
        entityMessageDto.setRiskDto(riskDto);

        List<Long> attachmentIds = getAttachmentIds(layer);
        entityMessageDto.setAttachmentIds(attachmentIds);

        List<ParamFieldDto> paramFields = mapper.mapToDtos(fields);
        paramFields.forEach(paramFieldDto -> {
            paramFieldDto.setBusinessEntityDtos(null);
            paramFieldDto.setBusinessEntityIds(null);
        });
        entityMessageDto.setParamFields(paramFields);
        entityMessageDto.setRiskId(layer.getRisk().getId());
        entityMessageDto.setEntityId(entityId);
        return entityMessageDto;
    }

    private List<Long> getAttachmentIds(Layer layer) {
        if (!CollectionUtils.isEmpty(layer.getAttachments())) {
            return layer.getAttachments().stream().filter(attachment -> !attachment.isSent())
                    .map(attachment -> attachment.getId()).collect(Collectors.toList());
        }
        return null;
    }

    private String getJsonString(EntityMessageDto entityMessageDto) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(entityMessageDto);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error when converting object to json string.");
            throw new RecordException("Error when converting object to json string.");
        }
    }

    private ResponseEntity<EntityMessageDto> sendMessageToEntity(EntityMessageDto entityMessageDto, String apiUrl) {
        try {
            return restTemplate.postForEntity(apiUrl, entityMessageDto, EntityMessageDto.class);
        } catch (RestClientResponseException ex) {
            LOGGER.error(" Rest API error when calling inbox service for the entity : {}", entityMessageDto.getEntityId());
        }
        return null;
    }
}
