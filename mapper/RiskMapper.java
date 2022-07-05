package com.record.mapper;

import com.record.dto.RiskDto;
import com.record.model.Risk;
import com.record.service.SystemParameterService;
import com.record.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class RiskMapper {

    @Value("${date.format}")
    private String dateFormat;

    @Autowired
    private LayerMapper layerMapper;

    @Autowired
    private SystemParameterService systemParameterService;


    public Risk mapToModel(RiskDto riskDto) {
        Risk risk = new Risk();
        risk.setName(riskDto.getRiskName());
        risk.setForm(riskDto.getForm());
        risk.setLob(riskDto.getLob());
        Date inceptionDate = getFormattedDate(riskDto.getPolicyInceptionDate());
        Date endDate = getFormattedDate(riskDto.getPolicyExpiryDate());
        risk.setPolicyInceptionDate(new java.sql.Date(inceptionDate.getTime()));
        risk.setPolicyEndDate(new java.sql.Date(endDate.getTime()));
        risk.setUwYear(riskDto.getUwYear());
        risk.setOriginId(riskDto.getOriginId());

        Integer riskSequence = systemParameterService.getRiskSequence();
        if(riskDto.getRiskId() == null) {
            String riskId = riskDto.getLob() + "_" + riskDto.getUwYear() + "_" + riskSequence;
            risk.setRiskId(riskId);
        } else {
            risk.setRiskId(riskDto.getRiskId());
        }
        return risk;
    }

    public RiskDto mapToDto(Risk risk) {
        RiskDto riskDto = new RiskDto();
        riskDto.setId(risk.getId());
        riskDto.setRiskName(risk.getName());
        riskDto.setLob(risk.getLob());
        riskDto.setForm(risk.getForm());
        riskDto.setUwYear(risk.getUwYear());
        riskDto.setOriginId(risk.getOriginId());
        String inceptionDate = DateUtil.convertSqlDateToString(risk.getPolicyInceptionDate());
        String endDate = DateUtil.convertSqlDateToString(risk.getPolicyEndDate());
        riskDto.setPolicyInceptionDate(inceptionDate);
        riskDto.setPolicyExpiryDate(endDate);
        riskDto.setRiskId(risk.getRiskId());
        return riskDto;
    }


    private Date getFormattedDate(String policyInceptionDate) {
        try {
            String[] dateStr = policyInceptionDate.split(" ");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return simpleDateFormat.parse(dateStr[0]);
        } catch (ParseException e) {
            //TODO: log and Handle error
        }
        return null;
    }
}
