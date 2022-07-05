package com.record.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PremiumDto {

    private Long id;
    private BigDecimal installmentPercent;
    private boolean isAdjustmentPremium;
    private String ppwDate;
    private int installmentNum;

    public BigDecimal getInstallmentPercent() {
        return installmentPercent;
    }

    public void setInstallmentPercent(BigDecimal installmentPercent) {
        this.installmentPercent = installmentPercent;
    }

    public boolean isAdjustmentPremium() {
        return isAdjustmentPremium;
    }

    public void setAdjustmentPremium(boolean adjustmentPremium) {
        isAdjustmentPremium = adjustmentPremium;
    }

    public String getPpwDate() {
        return ppwDate;
    }

    public void setPpwDate(String ppwDate) {
        this.ppwDate = ppwDate;
    }

    public int getInstallmentNum() {
        return installmentNum;
    }

    public void setInstallmentNum(int installmentNum) {
        this.installmentNum = installmentNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
