package com.record.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class RiskDto {

    private Long id;
    @NotNull
    @Size(max = 50)
    private String riskName;
    @NotNull
    @Size(max = 50)
    private String form;
    @NotNull
    private String policyInceptionDate;
    private String policyExpiryDate;
    private String lob;
    private String uwYear;
    private String riskId;
    private String originId;
    private List<LayerDto> layerDtos;

    public String getRiskName() {
        return riskName;
    }

    public void setRiskName(String riskName) {
        this.riskName = riskName;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getPolicyInceptionDate() {
        return policyInceptionDate;
    }

    public void setPolicyInceptionDate(String policyInceptionDate) {
        this.policyInceptionDate = policyInceptionDate;
    }

    public String getPolicyExpiryDate() {
        return policyExpiryDate;
    }

    public void setPolicyExpiryDate(String policyExpiryDate) {
        this.policyExpiryDate = policyExpiryDate;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public String getRiskId() {
        return riskId;
    }

    public void setRiskId(String riskId) {
        this.riskId = riskId;
    }

    public List<LayerDto> getLayerDtos() {
        return layerDtos;
    }

    public void setLayerDtos(List<LayerDto> layerDtos) {
        this.layerDtos = layerDtos;
    }

    public String getUwYear() {
        return uwYear;
    }

    public void setUwYear(String uwYear) {
        this.uwYear = uwYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }
}