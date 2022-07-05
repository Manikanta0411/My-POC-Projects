package com.record.model;

import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.record.entity.BusinessEntityOp;

@Entity
@Table(name = "PPW")
public class PPW extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NUM_OF_INSTALLMENTS", nullable = false)
    private int numberOfInstallments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LAYER_ID", nullable = false)
    private Layer layer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ppw_entity_link",
            joinColumns = @JoinColumn(name = "ppw_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "entity_id", referencedColumnName = "id"))
    private Collection<BusinessEntityOp> businessEntities;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "ppw", fetch = FetchType.LAZY)
    private Collection<PpwPremium> ppwPremiums;

    private String originId;

    @Column(name ="DEPOSIT_PREMIUM_PERCENT")
    private BigDecimal depositPremiumPercent;

    
    public BigDecimal getDepositPremiumPercent() {
		return depositPremiumPercent;
	}

	public void setDepositPremiumPercent(BigDecimal depositPremiumPercent) {
		this.depositPremiumPercent = depositPremiumPercent;
	}

    @Transient
    private Collection businessEntityIds;
    
    private String ppwCode;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<BusinessEntityOp> getBusinessEntities() {
        return businessEntities;
    }

    public void setBusinessEntities(Collection<BusinessEntityOp> businessEntities) {
        this.businessEntities = businessEntities;
    }

    public Collection getBusinessEntityIds() {
        return businessEntityIds;
    }

    public void setBusinessEntityIds(Collection businessEntityIds) {
        this.businessEntityIds = businessEntityIds;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public Collection<PpwPremium> getPpwPremiums() {
        return ppwPremiums;
    }

    public void setPpwPremiums(Collection<PpwPremium> ppwPremiums) {
        this.ppwPremiums = ppwPremiums;
    }

    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(int numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

	public String getPpwCode() {
		return ppwCode;
	}

	public void setPpwCode(String ppwCode) {
		this.ppwCode = ppwCode;
	}
    
    
    
}
