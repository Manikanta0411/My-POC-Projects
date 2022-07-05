package com.record.model;

import javax.persistence.*;

@Entity
@Table(name = "TRANSACTION_TEMPLATE")
public class TransactionTemplate extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TEMPLATE_ID", nullable = false)
    private Long templateId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
}
