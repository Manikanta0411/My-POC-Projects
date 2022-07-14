package com.record.service;

import static com.record.constant.LastOpIndicator.C;
import static com.record.constant.LastOpIndicator.I;
import static com.record.constant.LastOpIndicator.U;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.record.dto.BusinessEntityDto;
import com.record.dto.MessageTransDto;
import com.record.entity.BusinessEntityOp;
import com.record.entity.ParamEntityLink;
import com.record.entity.PpwEntityLink;
import com.record.exception.ResourceNotFoundException;
import com.record.model.Layer;
import com.record.model.MessageTrans;
import com.record.model.PPW;
import com.record.model.ParamField;
import com.record.model.PpwPremium;
import com.record.model.PpwPremiumHistory;
import com.record.model.Risk;
import com.record.repository.BusinessEntityRepository;
import com.record.repository.LayerRepository;
import com.record.repository.MessageTransRepository;
import com.record.repository.ParamEntityLinkRepository;
import com.record.repository.ParamFieldRepository;
import com.record.repository.PpwEntityLinkRepository;
import com.record.repository.PpwPremiumHistoryRepository;
import com.record.repository.PpwPremiumRepository;
import com.record.repository.PpwRepository;
import com.record.repository.RiskRepository;
import com.record.util.MapperUtil;

@Service
public class LayerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LayerService.class);

    @Autowired
    private LayerRepository layerRepository;

    @Autowired
    private RiskRepository riskRepository;

    @Autowired
    private ParamFieldHistoryService paramFieldHistoryService;

    @Autowired
    private PpwPremiumRepository ppwPremiumRepository;

    @Autowired
    private PpwPremiumHistoryRepository ppwPremiumHistoryRepository;

    
    @Autowired
    private ParamFieldRepository paramFieldRepository;

    @Autowired
    private BusinessEntityRepository businessEntityRepository;

    @Autowired
    private OutboxService outboxService;

    @Autowired
    private PpwRepository ppwRepository;
    
    @Autowired
    private MessageTransRepository messageTransRepository;
    
    @Autowired
    private TraversalService traversalService;
    
    @Autowired
    private ParamEntityLinkRepository paramEntityLinkRepository;
    
    @Autowired
    private SystemParameterService parameterService;
    
    @Autowired
    private PpwEntityLinkRepository ppwEntityLinkRepository;
    
   //@Transactional
    public Layer createLayer(Long riskId, Layer layer) {
        Optional<Risk> riskOption = riskRepository.findById(riskId);
        if (riskOption.isPresent()) {
            Risk risk = riskOption.get();
            List<Layer> layers = risk.getLayers();
            layer.setCreatedDate(new Date());
            layer.setUpdatedDate(new Date());
            layer.setLastOpIndicator(I);
            layers.add(layer);
            risk.setLayers(layers);
            layer.setRisk(risk);
            layer.setOriginId(UUID.randomUUID().toString());
            Layer savedLayer = layerRepository.save(layer);
            saveParamFields(layer);
            savePpw(savedLayer);
            riskRepository.save(risk);
        } else {
            LOGGER.error("Error! No risk found to create layer.");
            throw new ResourceNotFoundException("Error! Risk does not exist");
        }

        return layer;
    }
  
    private void savePpw(Layer layer) {
        List<PPW> ppws = layer.getPpws();
        for (PPW ppw : ppws) {
            ppw.setCreatedDate(new Date());
            ppw.setLastOpIndicator(I);
            ppw.setCreatedBy("user");
            
            List<PpwPremium> premiums = new ArrayList<>();
            for (PpwPremium ppwPremium : ppw.getPpwPremiums()) {
            	premiums.add(savePpwPremiumData(ppw,ppwPremium));
            }
            ppw.setPpwPremiums(premiums);
            ppw.setPpwCode("PPW_"+parameterService.getPpwSequence());
            ppw.setOriginId(UUID.randomUUID().toString());
            ppwRepository.save(ppw);
            
            List<BusinessEntityOp> businessEntities = businessEntityRepository.findByIdIn(ppw.getBusinessEntityIds());

			if (businessEntities != null) {
				businessEntities.stream().forEach(bEntity -> {
					LOGGER.debug("PPWId :" + ppw.getId() + " BusinessEntity id : " + bEntity.getId());
					savePpwEntityLinkData(ppw, bEntity);
				});
			}
            

        }
    }
    
    private void savePpwEntityLinkData(PPW ppw, BusinessEntityOp bEntity)
    {
    	PpwEntityLink ppwEntityLink=new PpwEntityLink();
    	ppwEntityLink.setEntityId(bEntity.getId());
    	ppwEntityLink.setPpwId(ppw.getId());
    	ppwEntityLink.setStatus(true);
    	ppwEntityLinkRepository.save(ppwEntityLink);

    }
    
    private PpwPremium savePpwPremiumData(PPW ppw,PpwPremium ppwPremium)
    {
    	PpwPremium premium = new PpwPremium();
        premium.setInstallmentNum(ppwPremium.getInstallmentNum());
        
        premium.setPremiumDate(ppwPremium.getPremiumDate());
        
        premium.setPercentage(ppwPremium.getPercentage());
        premium.setAmount(ppwPremium.getAmount());
        premium.setPpw(ppw);
        premium.setAdjustedPremium(ppwPremium.isAdjustedPremium());
        ppwPremiumRepository.save(premium);
        return premium;
        
    }
  
	private void saveParamFields(Layer layer) {

		List<ParamField> paramFields = layer.getParamFields();
		HashMap<Long, StringBuilder> tempEntity = new HashMap<Long, StringBuilder>();

		Long totalEntities = layer.getReqEntities().stream().count();

		layer.getReqEntities().stream().forEach(entityId -> {
			paramFields.forEach(paramField -> {
				if (totalEntities.equals(paramField.getBusinessEntities().stream().count())) {
					paramField.setOriginId(saveParmaFieldEnttity(paramField).getOriginId());
					if (tempEntity.containsKey(entityId)) {
						StringBuilder lastValue = tempEntity.get(entityId);
						tempEntity.put(entityId, lastValue.append(",").append(paramField.getId().toString()));
					} else {
						tempEntity.put(entityId, new StringBuilder(paramField.getId().toString()));
					}
				} else {
					if (paramField.getBusinessEntities().stream().map(BusinessEntityOp::getId)
							.anyMatch(x -> x == entityId)) {
						paramField.setOriginId(saveParmaFieldEnttity(paramField).getOriginId());
						if (tempEntity.containsKey(entityId)) {
							StringBuilder lastValue = tempEntity.get(entityId);
							tempEntity.put(entityId, lastValue.append(",").append(paramField.getId().toString()));
						} else {
							tempEntity.put(entityId, new StringBuilder(paramField.getId().toString()));
						}

					}

				}

			});

		});

		tempEntity.entrySet().stream().forEach(map -> {
			System.out.println("Key :" + map.getKey() + " " + " Value :" + map.getValue());
			BusinessEntityOp busEntity = new BusinessEntityOp();
			busEntity.setId(map.getKey());
			ParamEntityLink pp = new ParamEntityLink();
			pp.setBusinessEntity(busEntity);
			pp.setFieldIds(map.getValue().toString());
			pp.setLayer(layer);
			pp.setRisk(layer.getRisk());
			pp.setStatus("ACTIVE");
			paramEntityLinkRepository.save(pp);
		});

	}
	
	private ParamField saveParmaFieldEnttity(ParamField paramField) {
		List<BusinessEntityOp> businessEntities = businessEntityRepository.findByIdIn(paramField.getBusinessEntityIds());
		paramField.setBusinessEntities(businessEntities);
		paramField.setLastOpIndicator(I);
		paramField.setCreatedDate(new Date());
		paramField.setUpdatedDate(new Date());
		paramField.setSent(false);
		paramField.setFieldCode("P_"+parameterService.getParamFieldSequence());
		paramField.setOriginId(UUID.randomUUID().toString());
		return paramFieldRepository.save(paramField); 
		
	}

    public Layer getLayer(Long layerId) {
        return layerRepository.findById(layerId).orElseThrow(() -> {
            LOGGER.error("Error! Layer not found with id : " + layerId);
            return new ResourceNotFoundException("Error! Requested layer not found");
        });
    }

  //  @Transactional
    public void updateLayer(Layer layer, Long layerId) {
        Layer layerFromDB = layerRepository.findById(layerId).orElseThrow(() -> {
            LOGGER.debug("Error! Layer not found with id : " + layerId);
            return new ResourceNotFoundException("Error! layer not found");
        });
        updateParamFieldsNew(layer, layerFromDB);
        updatePpw(layer, layerId);
    }

    private void updatePpw(Layer layer, Long layerId) {
        layer.getPpws().stream().forEach(ppw ->{
        	
            Collection<PpwPremium> ppwPremiums = ppw.getPpwPremiums();
            
            Collection<PpwPremium> newPremiums = new ArrayList<>();
            
            PPW ppwObject = ppwRepository.findByPpwCode(ppw.getPpwCode()).orElseThrow(() -> {
                LOGGER.debug("Error! PPW Code not found with id : " + layerId);
                return new ResourceNotFoundException("Error! PPW Code not found");
            });
            

            //create history of ppw premium data
            ppwObject.getPpwPremiums().stream().forEach(ppwObj -> {
            	PpwPremiumHistory ppwPremiumHistory=new PpwPremiumHistory();
            	ppwPremiumHistory.setAmount(ppwObj.getAmount());
            	ppwPremiumHistory.setInstallmentNum(ppwObj.getInstallmentNum());
            	ppwPremiumHistory.setPercentage(ppwObj.getPercentage());
            	ppwPremiumHistory.setPremiumDate(ppwObj.getPremiumDate());
            	ppwPremiumHistory.setPpw(ppwObj.getPpw());
            	ppwPremiumHistory.setAdjustedPremium(ppwObj.isAdjustedPremium());
            	ppwPremiumHistoryRepository.save(ppwPremiumHistory);
            	
            });
            
            //delete all ppwpremium data 
            ppwPremiumRepository.deleteAll(ppwObject.getPpwPremiums());
            
            //add new data to ppwPremium table
            ppwPremiums.forEach(ppwPremium -> {
                PpwPremium premium = new PpwPremium();
                premium.setAmount(ppwPremium.getAmount());
                premium.setAdjustedPremium(ppwPremium.isAdjustedPremium());
                premium.setPercentage(ppwPremium.getPercentage());
                premium.setPremiumDate(ppwPremium.getPremiumDate());
                premium.setInstallmentNum(ppwPremium.getInstallmentNum());
                premium.setPpw(ppwObject);
                ppwPremiumRepository.save(premium);
                newPremiums.add(premium);
            });
            
            List<PpwEntityLink> ppwEntityLinkList = ppwEntityLinkRepository.findByPpwId(ppwObject.getId());
            
            if(ppwEntityLinkList.size()>0)
			{

				List<BusinessEntityOp> newEntityList = businessEntityRepository.findByIdIn(ppw.getBusinessEntityIds());
				if (newEntityList.size() > 0) {
					ppwEntityLinkList.stream().forEach(pwwEntityLink -> {
						newEntityList.stream().forEach(newEntity -> {
							LOGGER.debug("*********** ppwEntity :" + pwwEntityLink.getEntityId() + " newEntityId : "
									+ newEntity.getId());
							if (!pwwEntityLink.getEntityId().equals(newEntity.getId())) {
								pwwEntityLink.setStatus(false);
								ppwEntityLinkRepository.save(pwwEntityLink);
								savePpwEntityLinkData(ppwObject, newEntity);
							} else {
								savePpwEntityLinkData(ppwObject, newEntity);
							}

						});

					});

				}

			}
            ppwObject.setPpwPremiums(newPremiums);
            ppwRepository.save(ppwObject);
        });
        

    }

    

	private void updateParamFieldsNew(Layer layer, Layer layerFromDB) {
    	
    	List<ParamField> savedValues= paramFieldRepository.findAllByLayer(layerFromDB);
    	
    	if(!savedValues.isEmpty())
		{
			List<ParamField> newValues = layer.getParamFields();
			
			HashMap<String, StringBuilder> tempEntity = new HashMap<String, StringBuilder>();
			
			newValues.forEach(newValue -> {
				if (newValue.getFieldCode() != null) {
					Optional<ParamField> savedParamField = paramFieldRepository.findByFieldCode(newValue.getFieldCode());
					
					if (savedParamField.isPresent()) {
						if (newValue.getOprationType().equals("U")) {
							ParamField pp = savedParamField.get();
							pp.setFieldValue(newValue.getFieldValue());
							pp.setLastOpIndicator(U);
							pp.setLayer(layerFromDB);
							pp.setBusinessEntities(newValue.getBusinessEntities());
							pp.setUpdatedDate(new Date());
							paramFieldRepository.save(pp);
							newValue.getBusinessEntities().forEach(enity -> {
								String key = enity.getId().toString();
								if (tempEntity.containsKey(key)) {
									StringBuilder lastValue = tempEntity.get(key);
									tempEntity.put(key, lastValue.append(",").append(pp.getId().toString()));
								} else {
									tempEntity.put(key, new StringBuilder(pp.getId().toString()));
								}

							});

						}else if(newValue.getOprationType().equals("C"))
						{
							ParamField newParamField = new ParamField();
							newParamField.setFieldKey(newValue.getFieldKey());
							newParamField.setFieldValue(newValue.getFieldValue());
							newParamField.setFieldType(newValue.getFieldType());
							newParamField.setCreatedDate(new Date());
							newParamField.setUpdatedDate(new Date());
							newParamField.setLastOpIndicator(C);
							newParamField.setLayer(layerFromDB);
							newParamField.setBusinessEntities(newValue.getBusinessEntities());
							newParamField.setUpdatedDate(new Date());
							newParamField.setFieldCode("P_" + parameterService.getParamFieldSequence());
							newParamField.setClonedOriginId(savedParamField.get().getOriginId());
							paramFieldRepository.save(newParamField);
							newValue.getBusinessEntities().forEach(enity -> { 
								String key = enity.getId().toString();
								if (tempEntity.containsKey(key)) {
									StringBuilder lastValue = tempEntity.get(key);
									tempEntity.put(key, lastValue.append(",").append(newParamField.getId().toString()));
								} else {
									tempEntity.put(key, new StringBuilder(newParamField.getId().toString()));
								}

							});
						
						}

					}

				} else {
					System.out.println(newValue.getFieldKey());
					ParamField newParamField = new ParamField();
					newParamField.setFieldKey(newValue.getFieldKey());
					newParamField.setFieldValue(newValue.getFieldValue());
					newParamField.setFieldType(newValue.getFieldType());
					newParamField.setCreatedDate(new Date());
					newParamField.setUpdatedDate(new Date());
					newParamField.setLastOpIndicator(I);
					newParamField.setLayer(layerFromDB);
					newParamField.setBusinessEntities(newValue.getBusinessEntities());
					newParamField.setUpdatedDate(new Date());
					newParamField.setFieldCode("P_" + parameterService.getParamFieldSequence());
					paramFieldRepository.save(newParamField);
					
					newValue.getBusinessEntities().forEach(enity -> { 
						
						String key = enity.getId().toString();
						if (tempEntity.containsKey(key)) {
							StringBuilder lastValue = tempEntity.get(key);
							tempEntity.put(key, lastValue.append(",").append(newParamField.getId().toString()));
						} else {
							tempEntity.put(key, new StringBuilder(newParamField.getId().toString()));
						}

					});
				}

			});
			tempEntity.entrySet().stream().forEach(map -> {
				System.out.println("Key :" + map.getKey() + " " + " Value :" + map.getValue());
			});
			System.out.println(layer.getReqEntities().stream().count());
			
			layer.getReqEntities().stream().forEach(entityId->{
				System.out.println(layerFromDB.getId()+" "+entityId+" "+layerFromDB.getRisk().getId());
				
				
				Optional<ParamEntityLink> paramEntityLink= paramEntityLinkRepository.findByLayerAndBusinessEntityAndRiskAndStatus(layerFromDB.getId(),entityId,layerFromDB.getRisk().getId(),"ACTIVE");
			
				if(paramEntityLink.isPresent())
				  {
					
					if(!tempEntity.get(entityId.toString()).toString().equals(paramEntityLink.get().getFieldIds()))
  					{
						    ParamEntityLink ee=paramEntityLink.get();
						    ee.setStatus("LACTIVE");
						    paramEntityLinkRepository.save(ee);
						    BusinessEntityOp busEntity = new BusinessEntityOp();
							busEntity.setId(entityId);
							ParamEntityLink pp = new ParamEntityLink();
							pp.setBusinessEntity(busEntity);
							pp.setFieldIds(tempEntity.get(entityId.toString()).toString());
							pp.setLayer(layerFromDB);
							pp.setRisk(layerFromDB.getRisk());
							pp.setStatus("ACTIVE");
							paramEntityLinkRepository.save(pp);
							  
						  
					}
					  
					  
				  }else
				  {
					    BusinessEntityOp busEntity = new BusinessEntityOp();
						busEntity.setId(entityId);
						ParamEntityLink pp = new ParamEntityLink();
						pp.setBusinessEntity(busEntity);
						pp.setFieldIds(tempEntity.get(entityId.toString()).toString());
						pp.setLayer(layerFromDB);
						pp.setRisk(layerFromDB.getRisk());
						pp.setStatus("ACTIVE");
						paramEntityLinkRepository.save(pp);
						
				  }
				
				
				/*
				if(paramEntityLink.isPresent())
			  {
				  ParamEntityLink ee=paramEntityLink.get();
				  System.out.println(tempEntity.get(entityId.toString()));
				  if(tempEntity.get(entityId.toString())==null) //if no fileds selected at the time of update for entity delete the entity from link table
				  {
					  paramEntityLinkRepository.delete(paramEntityLink.get());
				  }else
				  {  
				  ee.setFieldIds(tempEntity.get(entityId.toString()).toString());
				  System.out.println(ee.getId());
				  paramEntityLinkRepository.save(ee);
				  }
				  }else
			  {
				    BusinessEntityOp busEntity = new BusinessEntityOp();
					busEntity.setId(entityId);
					ParamEntityLink pp = new ParamEntityLink();
					pp.setBusinessEntity(busEntity);
					pp.setFieldIds(tempEntity.get(entityId.toString()).toString());
					pp.setLayer(layerFromDB);
					pp.setRisk(layerFromDB.getRisk());
					paramEntityLinkRepository.save(pp);
			  }
				*/
				
			    
				
				
				
				
			});
		

		}
    }
    
    
    private void updateParamFields(Layer layer, Layer layerFromDB) {
        List<ParamField> paramFields = layer.getParamFields();
        paramFields.forEach(param -> {
            ParamField paramField = paramFieldRepository.findById(param.getId()).get();
            paramField.setFieldValue(param.getFieldValue());
            paramField.setLastOpIndicator(U);
            paramField.setLayer(layerFromDB);
            paramField.setBusinessEntities(param.getBusinessEntities());
            paramField.setUpdatedDate(new Date());
            paramFieldRepository.save(paramField);
        });
    }

    public void circulateLayer(Long layerId,List<BusinessEntityDto> reqEntities) {
        Layer layer = getLayer(layerId);
        outboxService.saveOutboxNew(layer,reqEntities);
        paramFieldHistoryService.createHistoryRecord(layer,reqEntities);
        //TODO: call API to update inbox.
    }

    public Optional<Layer> findByOriginId(String originId) {
        return layerRepository.findByOriginId(originId);
    }

    public List<Layer> getLayers(Long riskId) {
        return layerRepository.findByRiskId(riskId);
    }

    @Transactional
    public void save(Layer newLayer) {
        layerRepository.save(newLayer);
    }
    
    
    public MessageTransDto getMessageUsingOriginMessageId(String OriginMsgid)
	{
		Optional<MessageTrans> msg = messageTransRepository.findByOriginMessageId(OriginMsgid);
		if (msg.isPresent()) {
			return MapperUtil.convertModelToEntity(msg.get(), MessageTransDto.class);
		} else {
			throw new EntityNotFoundException("Message " + OriginMsgid + "not found !");
		}
	}
    
    
    /*
    public void savePPW(List<PpwDto> reqPPW,Long layerId)
    {
    	Optional<Layer> layer= layerRepository.findById(layerId);
    	if(layer.isPresent())
    	{
    		reqPPW.stream().forEach(ppwReq -> {
    			
    			List<BusinessEntity> businessEntities = businessEntityRepository.findByIdIn(ppwReq.getBusinessEntityIds());
    			PPW newPPW=new PPW();
    			newPPW.setLastOpIndicator(I);
    			newPPW.setLayer(layer.get()); 
    			newPPW.setCreatedBy("user");
    			newPPW.setBusinessEntities(businessEntities);
    			ppwRepository.save(newPPW);
                
    			List<PpwPremium> premiums = new ArrayList<>();
                for (PremiumDto premiumDto : ppwReq.getPremiumListDto()) {
                    PpwPremium premium = new PpwPremium();
                    premium.setInstallmentNum(premiumDto.getInstallmentNum());
                   // premium.setPremiumDate(premiumDto.getPpwDate()); 
                    premium.setPercentage(premiumDto.getInstallmentPercent());
                    premium.setAmount(new BigDecimal(2000.0));
                   
                    premium.setPpw(newPPW);
                    premium.setAdjustedPremium(premiumDto.isAdjustmentPremium());
                    ppwPremiumRepository.save(premium);
                    premiums.add(premium);
                }
                newPPW.setPpwPremiums(premiums);
                newPPW.setOriginId(newPPW.getId());
    		});
    		
    		
    	}else
    	{
    		throw new EntityNotFoundException("Layer " + layerId + "not found !");
    	}
    }
    */
    public List<ParamEntityLink> getParamEntityLinks(Layer layer){
		List<ParamEntityLink> paramEntityLinkList = paramEntityLinkRepository.findByLayer(layer);
		return paramEntityLinkList;
		
	}
    
}
