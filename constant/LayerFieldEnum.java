package com.record.constant;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.stream.Stream;

public enum LayerFieldEnum {
    INSURED_NAME("insuredName", 30, "Insured Name", true),
    ADDRESS_OF_INSURED("addressOfInsured", 300, "Address Of Insured", true),
    PERILS_INSURED("perilsInsured", null, "Perils Insured", false),
    OCCUPANCY("occupancy", 300, "Occupancy", true),
    CURRENCY("currency", null, "Currency", false),
    TOTAL_SUM_INSURED("totalSumInsured", null, "Total Sum Insured", false),
    LIMIT_OF_LIABILITY("limitOfLiability", null, "Limit Of Liability", false),
    PREMIUM_RATE("premiumRate", null, "Premium Rate", false),
    PREMIUM_RATE_FIELD("premiumRateSelection", null, "Premium Rate Field", false),
    TOTAL_PREMIUM("totalPremium", null, "Total Premium", false),
    SAL("sal", null, "SAL", false),
    CEDING_COMMISSION("cedingCommission", null, "Ceding Commission", false),
    LOSS_HISTORY("lossHistory", null, "Loss History", false),
    FAC_REQUIRED("facRequired", null, "Fac Required", false);

    private String fieldName;
    private Integer maxSize;
    private String label;
    private boolean isValidationRequired;

    private static final HashMap<String,LayerFieldEnum> LOOKUP = new HashMap<String,LayerFieldEnum>();
    
    static {
    	for(LayerFieldEnum type : EnumSet.allOf(LayerFieldEnum.class)) {
    		LOOKUP.put(type.getFieldName(), type);
    	}
    }
    
    LayerFieldEnum(String fieldName, Integer maxSize, String label, boolean isValidationRequired) {
        this.fieldName = fieldName;
        this.maxSize = maxSize;
        this.label = label;
        this.isValidationRequired = isValidationRequired;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public String getLabel() {
        return label;
    }

    public boolean isValidationRequired() {
        return isValidationRequired;
    }

    public static Stream<LayerFieldEnum> stream() {
        return Stream.of(LayerFieldEnum.values());
    }
    
    public static String getLabelByFieldName(String fieldName) {
    	String res = null;
    	
    	LayerFieldEnum layerFieldEnum = get(fieldName);
    	if(layerFieldEnum!=null) {
    		res = layerFieldEnum.getLabel();
    	}
    	return res; 
    }

    public static LayerFieldEnum get(String fieldName) {
    	LayerFieldEnum layerFieldEnum = LOOKUP.get(fieldName);
    	return layerFieldEnum;
    }
}
