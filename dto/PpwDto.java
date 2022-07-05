package com.record.dto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class PpwDto {
	
	private Long id;
    private int numOfInstallment;
    private BigDecimal depositPremiumPercent;
    private List<PremiumDto> premiumListDto;
    private List<BusinessEntityDto> businessEntityDtos;
    private Collection<Long> businessEntityIds;
    private String originId;
    private BigDecimal totalPremiumAmount;
    private String ppwCode;
    
    
    public String getPpwCode() {
		return ppwCode;
	}

	public void setPpwCode(String ppwCode) {
		this.ppwCode = ppwCode;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumOfInstallment() {
        return numOfInstallment;
    }

    public void setNumOfInstallment(int numOfInstallment) {
        this.numOfInstallment = numOfInstallment;
    }

    public BigDecimal getDepositPremiumPercent() {
        return depositPremiumPercent;
    }

    public void setDepositPremiumPercent(BigDecimal depositPremiumPercent) {
        this.depositPremiumPercent = depositPremiumPercent;
    }

    public List<PremiumDto> getPremiumListDto() {
        return premiumListDto;
    }

    public void setPremiumListDto(List<PremiumDto> premiumListDto) {
        this.premiumListDto = premiumListDto;
    }

    public List<BusinessEntityDto> getBusinessEntityDtos() {
        return businessEntityDtos;
    }

    public void setBusinessEntityDtos(List<BusinessEntityDto> businessEntityDtos) {
        this.businessEntityDtos = businessEntityDtos;
    }

    public Collection<Long> getBusinessEntityIds() {
        return businessEntityIds;
    }

    public void setBusinessEntityIds(Collection<Long> businessEntityIds) {
        this.businessEntityIds = businessEntityIds;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

	public BigDecimal getTotalPremiumAmount() {
		return totalPremiumAmount;
	}

	public void setTotalPremiumAmount(BigDecimal totalPremiumAmount) {
		this.totalPremiumAmount = totalPremiumAmount;
	}
    
    
    
    
}
