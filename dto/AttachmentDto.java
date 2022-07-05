package com.record.dto;

import javax.persistence.*;
import java.sql.Blob;
import java.util.List;

public class AttachmentDto {
    private Long id;
    private List<Long> businessEntityIds;
    private String fileName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getBusinessEntityIds() {
        return businessEntityIds;
    }

    public void setBusinessEntityIds(List<Long> businessEntityIds) {
        this.businessEntityIds = businessEntityIds;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
