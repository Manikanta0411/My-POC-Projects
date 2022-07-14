package com.record.service;

import com.record.model.Layer;
import com.record.model.PPW;
import com.record.model.PpwPremium;
import com.record.repository.PpwPremiumRepository;
import com.record.repository.PpwRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.record.constant.LastOpIndicator.I;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PpwService {

    @Autowired
    private PpwRepository ppwRepository;
    
    @Autowired
    private PpwPremiumRepository ppwPremiumRepository;
    
    @Autowired
    private SystemParameterService parameterService;
    

    public Optional<PPW> findByOriginId(String originId) {
        return ppwRepository.findByOriginId(originId);
    }
    
    

    public void savePpw(List<PPW> ppws,Layer layer) {
        for (PPW ppw : ppws) {
            //   List businessEntities = businessEntityRepository.findByIdIn(ppw.getBusinessEntityIds());
              // ppw.setBusinessEntities(businessEntities);
               ppw.setCreatedDate(new Date());
               ppw.setLastOpIndicator(I);
               ppw.setLayer(layer);
               ppw.setCreatedBy("user");
               ppw.setPpwCode("PPW_"+parameterService.getPpwSequence());
               List<PpwPremium> premiums = new ArrayList<>();
               ppwRepository.save(ppw);
               
               for (PpwPremium ppwPremium : ppw.getPpwPremiums()) {
                   PpwPremium premium = new PpwPremium();
                   premium.setInstallmentNum(ppwPremium.getInstallmentNum());
                   premium.setPremiumDate(ppwPremium.getPremiumDate());
                   premium.setPercentage(ppwPremium.getPercentage());
                   premium.setAmount(ppwPremium.getAmount());
                   premium.setPpw(ppw);
                   premium.setAdjustedPremium(ppwPremium.isAdjustedPremium());
                   ppwPremiumRepository.save(premium);
                   premiums.add(premium);
               }
               ppw.setPpwPremiums(premiums);
               //ppw.setPpwCode("PPW_"+parameterService.getPpwSequence());
               ppwRepository.save(ppw);
               ppw.setOriginId(ppw.getOriginId());
           }
       }
    
    
}
