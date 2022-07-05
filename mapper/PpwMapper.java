package com.record.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.record.dto.BusinessEntityDto;
import com.record.dto.PpwDto;
import com.record.dto.PremiumDto;
import com.record.entity.BusinessEntityOp;
import com.record.model.PPW;
import com.record.model.PpwPremium;
import com.record.util.DateUtil;
import com.record.util.Util;

@Component
public class PpwMapper {

    @Autowired
    private BusinessEntityMapper businessEntityMapper;

    public PpwDto mapToDto(PPW ppw) {
        PpwDto ppwDto = new PpwDto();
        List<PremiumDto> premiumDtoList = new ArrayList<>();
        ppwDto.setId(ppw.getId());
        ppwDto.setOriginId(ppw.getOriginId());
        BigDecimal sum = BigDecimal.ZERO;

        ppw.getPpwPremiums().forEach(ppwPremium -> {
            PremiumDto premiumDto = new PremiumDto();
            premiumDto.setAdjustmentPremium(ppwPremium.isAdjustedPremium());
            premiumDto.setInstallmentNum(ppwPremium.getInstallmentNum());
            premiumDto.setInstallmentPercent(ppwPremium.getPercentage());
            premiumDto.setPpwDate(DateUtil.convertSqlDateToString(ppwPremium.getPremiumDate()));
            premiumDtoList.add(premiumDto);
            //sum= sum.add(ppwPremium.getAmount());
        });
        
        ppwDto.setTotalPremiumAmount(ppw.getPpwPremiums().stream()
                .map(PpwPremium::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    
        Collection<BusinessEntityOp> businessEntities = ppw.getBusinessEntities();
        List<BusinessEntityDto> businessEntityDtos = businessEntityMapper.mapToDtos(businessEntities);
        ppwDto.setBusinessEntityDtos(businessEntityDtos);
        ppwDto.setPremiumListDto(premiumDtoList);
        ppwDto.setNumOfInstallment(ppw.getNumberOfInstallments());
        
        return ppwDto;
    }

    public PPW mapToModel(PpwDto ppwDto) {
        PPW ppw = new PPW();
        List<PpwPremium> premiums = new ArrayList<>();
        ppwDto.getPremiumListDto().forEach(premiumDto -> {
            PpwPremium premium = new PpwPremium();
            premium.setAdjustedPremium(premiumDto.isAdjustmentPremium());
            Date formattedDate = Util.getFormattedDate(premiumDto.getPpwDate(), "dd/MM/yyyy");
            premium.setPremiumDate(new java.sql.Date(formattedDate.getTime()));
            premium.setPercentage(premiumDto.getInstallmentPercent());
            premium.setInstallmentNum(premiumDto.getInstallmentNum());
            BigDecimal installmentPercent = premiumDto.getInstallmentPercent();
            premium.setAmount(ppwDto.getTotalPremiumAmount().multiply(installmentPercent).divide(new BigDecimal(100.0)));
            premium.setPpw(ppw);
            premiums.add(premium);
            
        });
        ppw.setPpwPremiums(premiums);
        ppw.setNumberOfInstallments(ppwDto.getNumOfInstallment());
        ppw.setDepositPremiumPercent(ppwDto.getDepositPremiumPercent());
        ppw.setOriginId(ppwDto.getOriginId());
        return ppw;
    }
}
