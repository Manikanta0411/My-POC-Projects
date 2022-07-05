package com.record.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ENTITY_API_CONFIG")
public class EntityApiConfig {
    @Id
    private Long id;
    @Column(name = "ENTITY_ID", nullable = false)
    private Long entityId;
    @Column(name = "ENDPOINT", nullable = false)
    private String endpoint;
    @Column(name = "HTTP_METHOD")
    private String httpMethod;
    @Column(name = "CONTENT_TYPE")
    private String contentType;
    @Column(name = "ACCEPT_HEADER")
    private String acceptHeader;
    @Column(name = "ACTIVE")
    private boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getAcceptHeader() {
        return acceptHeader;
    }

    public void setAcceptHeader(String acceptHeader) {
        this.acceptHeader = acceptHeader;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
