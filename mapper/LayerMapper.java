package com.record.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.record.dto.LayerDto;
import com.record.dto.ParamFieldDto;
import com.record.dto.PpwDto;
import com.record.dto.PremiumDto;
import com.record.model.Layer;
import com.record.model.PPW;
import com.record.model.ParamField;
import com.record.model.PpwPremium;
import com.record.util.DateUtil;

@Component
public class LayerMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(LayerMapper.class);
    public static final String STANDARD = "REGULAR";
    public static final String FIELD_PPW = "ppw";
    public static final String FIELD_PREMIUM_RATE_SELECTION = "premiumRateSelection";
    public static final String FIELD_TOTAL_PREMIUM = "totalPremium";
    public static final String FIELD_INSURED_NAME = "insuredName";
    public static final String FIELD_ADDRESS_OF_INSURED = "addressOfInsured";
    public static final String FIELD_CEDANT = "cedant";
    public static final String FIELD_PERILS_INSURED = "perilsInsured";
    public static final String FIELD_CURRENCY = "currency";
    public static final String FIELD_PREMIUM_RATE_FIELD = "premiumRateSelection";
    public static final String FIELD_DEDUCTIBLE = "deductible";
    public static final String FIELD_LOSS_HISTORY = "lossHistory";
    public static final String FIELD_INTEREST = "interest";
    public static final String FIELD_TOTAL_SUM_INSURED = "totalSumInsured";
    public static final String FIELD_LIMIT_OF_LIABILITY = "limitOfLiability";
    public static final String FIELD_PREMIUM_RATE = "premiumRate";
    public static final String FIELD_SAL = "sal";
    public static final String FIELD_CEDING_COMMISSION = "cedingCommission";
    public static final String FIELD_FAC_REQUIRED = "facRequired";
    public static final String FIELD_OCCUPANCY_REQUIRED = "occupancy";
    

    @Autowired
    private ParamFieldMapper paramFieldMapper;

    @Autowired
    private PpwMapper ppwMapper;

    public Layer mapToModel(LayerDto layerDto) {
        Layer layer = new Layer();
        layer.setName(layerDto.getName());
        layer.setOriginId(layerDto.getOriginId());
        layer.setReqEntities(layerDto.getEntitiesId());
        List<ParamFieldDto> fields = layerDto.getParamFieldDtos();
        List<ParamField> paramFields = new ArrayList<>();

        fields.forEach(fieldDto -> {
            if ("STANDARD".equals(fieldDto.getFieldType())) {
                if (FIELD_PPW.equals(fieldDto.getFieldKey())) {
                    populatePpw(layerDto, layer, fieldDto);
                }
                ParamField paramField = getParamField(fieldDto);
                if (paramField != null) {
                    paramField.setLayer(layer);
                    paramFields.add(paramField);
                }
            } else {
                ParamField paramField = paramFieldMapper.mapToModel(fieldDto);
                paramField.setLayer(layer);
                paramFields.add(paramField);
            }
        });
        layer.setParamFields(paramFields);
        return layer;
    }


    
    
    private void populatePpw(LayerDto layerDto, Layer layer, ParamFieldDto fieldDto) {
        ObjectMapper mapper = new ObjectMapper();
		/*
		 * PpwDto ppwDto = mapper.convertValue(fieldDto.getValueAsObject(),PpwDto.class);
		 * ppwDto.setBusinessEntityIds(fieldDto.getBusinessEntityIds());
		 * BigDecimal premiumRate = getPremiumRateSelection(layerDto, ppwDto); 
		 * PPW ppw = getPpw(ppwDto, premiumRate, layer);
		 * layer.setPpws(Arrays.asList(ppw));
		 */
        
        List<PPW> newPpwList=new ArrayList<>();
        fieldDto.getPpwDto().stream().forEach(valueObject ->{
        	PpwDto ppwDto = mapper.convertValue(valueObject,PpwDto.class);
        	ppwDto.setBusinessEntityIds(fieldDto.getBusinessEntityIds());
        	//BigDecimal premiumRate = getPremiumRateSelection(layerDto, ppwDto);
        	BigDecimal premiumRate = valueObject.getTotalPremiumAmount();
        	PPW ppw = getPpw(ppwDto, premiumRate, layer);
        	newPpwList.add(ppw);
        });
        layer.setPpws(newPpwList);
    
    }

    private BigDecimal getPremiumRateSelection(LayerDto layerDto, PpwDto ppwDto) {
        if (ppwDto.getNumOfInstallment() == 1) {
            return getTotalPremium(layerDto);
        }
        return getPremiumRateSelection(layerDto);
    }


    private BigDecimal getPremiumRateSelection(LayerDto layerDto) { //patam filed layerid totalPreium value 
        Optional<ParamFieldDto> paramField = layerDto.getParamFieldDtos().stream()
                .filter(dto -> FIELD_PREMIUM_RATE_SELECTION.equals(dto.getFieldKey())).findFirst();
        return paramField.isPresent() ? new BigDecimal(paramField.get().getFieldValue()) : null;
    }

    private PPW getPpw(PpwDto ppwDto, BigDecimal premium, Layer layer) {
        int numOfInstallment = ppwDto.getNumOfInstallment();
        PPW ppw = new PPW();
        if (ppwDto.getId() != null) {
            ppw.setId(ppwDto.getId());
        }
        if (numOfInstallment == 1) {
            PpwPremium ppwPremium = getPpwPremium(ppwDto, premium, ppw);
            ppw.setPpwPremiums(Arrays.asList(ppwPremium));
            return ppw;
        } else {
            List<PpwPremium> ppwPremiums = getPpwPremiums(ppwDto, premium, ppw);
            ppw.setPpwPremiums(ppwPremiums);
        }
        ppw.setBusinessEntityIds(ppwDto.getBusinessEntityIds());
        ppw.setUpdatedDate(new Date());
        ppw.setLayer(layer);
        ppw.setPpwCode(ppwDto.getPpwCode());
        ppw.setNumberOfInstallments(numOfInstallment);
        ppw.setDepositPremiumPercent(ppwDto.getDepositPremiumPercent());
        return ppw;
    }

    private PpwPremium getPpwPremium(PpwDto ppwDto, BigDecimal premium, PPW ppw) {
        PpwPremium ppwPremium = new PpwPremium();
        PremiumDto premiumDdo = ppwDto.getPremiumListDto().get(0);
        ppwPremium.setAdjustedPremium(premiumDdo.isAdjustmentPremium());
        Date formattedDate = DateUtil.getFormattedDate(premiumDdo.getPpwDate(), "dd/MM/yyyy"); 
        ppwPremium.setPremiumDate(new java.sql.Date(formattedDate.getTime()));
        ppwPremium.setPercentage(premiumDdo.getInstallmentPercent());
        ppwPremium.setPpw(ppw);
        ppwPremium.setAmount(premium);
        ppwPremium.setInstallmentNum(1);
        return ppwPremium;
    }

    private List<PpwPremium> getPpwPremiums(PpwDto ppwDto, BigDecimal premium, PPW ppw) {
        List<PpwPremium> ppwPremiums = new ArrayList<>();
        for (PremiumDto premiumDto : ppwDto.getPremiumListDto()) {
            PpwPremium ppwPremium = new PpwPremium();
            BigDecimal installmentPercent = premiumDto.getInstallmentPercent();
            
            ppwPremium.setAmount(premium.multiply(installmentPercent).divide(new BigDecimal(100.0)));
            
            ppwPremium.setInstallmentNum(premiumDto.getInstallmentNum());
            Date formattedDate = DateUtil.getFormattedDate(premiumDto.getPpwDate(), "dd/MM/yyyy");
            ppwPremium.setPremiumDate(new java.sql.Date(formattedDate.getTime()));
            ppwPremium.setPercentage(premiumDto.getInstallmentPercent());
            ppwPremium.setAdjustedPremium(premiumDto.isAdjustmentPremium());
            ppwPremium.setPpw(ppw);
            ppwPremiums.add(ppwPremium);
        }
        return ppwPremiums;
    }

    private BigDecimal getTotalPremium(LayerDto layerDto) {
        ParamFieldDto paramFieldDto = layerDto.getParamFieldDtos().stream().filter((dto) ->
                FIELD_TOTAL_PREMIUM.equals(dto.getFieldKey())
        ).findFirst().get();
        return new BigDecimal(paramFieldDto.getFieldValue());
    }

    private ParamField getParamField(ParamFieldDto fieldDto) {
        switch (fieldDto.getFieldKey()) {
            case FIELD_INSURED_NAME:
            case FIELD_ADDRESS_OF_INSURED:
            case FIELD_CEDANT:
            case FIELD_PERILS_INSURED:
            case FIELD_CURRENCY:
            case FIELD_PREMIUM_RATE_FIELD:
            case FIELD_DEDUCTIBLE:
            case FIELD_LOSS_HISTORY:
            case FIELD_OCCUPANCY_REQUIRED:            	
                return getParamField(fieldDto, "STRING");
            case FIELD_INTEREST:
            case FIELD_TOTAL_SUM_INSURED:
            case FIELD_LIMIT_OF_LIABILITY:
            case FIELD_PREMIUM_RATE:
            case FIELD_TOTAL_PREMIUM:
            case FIELD_SAL:
            case FIELD_CEDING_COMMISSION:
            case FIELD_FAC_REQUIRED:
                return getParamField(fieldDto, "DECIMAL");
            default:
                LOGGER.info("No field configured with name : " + fieldDto.getFieldKey());
                return null;
        }
    }

    private ParamField getParamField(ParamFieldDto fieldDto, String dataType) {
        //TODO: DataType check and field value parsing to be done
        fieldDto.setDataType(dataType);
        ParamField paramField = paramFieldMapper.mapToModel(fieldDto);
        return paramField;
    }

    public LayerDto mapToDto(Layer layer) {
        LayerDto layerDto = new LayerDto();
        layerDto.setName(layer.getName());
        layerDto.setId(layer.getId());
        List<ParamFieldDto> dtos = paramFieldMapper.mapToDtos(layer.getParamFields());
        layerDto.setOriginId(layer.getOriginId());
        List<PPW> ppws = layer.getPpws();
        
        List<PpwDto> ppwDtoList=new ArrayList<>();
        
        
        ppws.forEach(ppw -> {
        	PpwDto ppwDto = ppwMapper.mapToDto(ppw);
        	ppwDtoList.add(ppwDto);
         });
        

        ParamFieldDto paramFieldDto = new ParamFieldDto();
        paramFieldDto.setFieldKey(FIELD_PPW);
        paramFieldDto.setPpwDto(ppwDtoList);
        dtos.add(paramFieldDto);

        
        layerDto.setParamFieldDtos(dtos);
        return layerDto;
    }
}
