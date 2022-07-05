package com.record.model;

import com.record.constant.LastOpIndicator;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class BaseModel {
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "LAST_OP_IND", length = 10)
    @Enumerated(EnumType.STRING)
    private LastOpIndicator lastOpIndicator;

    @Column(name = "CREATED_BY", length = 10)
    private String createdBy;

    @Column(name = "LAST_OP_BY", length = 10)
    private String lastUpdatedBy;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LastOpIndicator getLastOpIndicator() {
        return lastOpIndicator;
    }

    public void setLastOpIndicator(LastOpIndicator lastOpIndicator) {
        this.lastOpIndicator = lastOpIndicator;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
