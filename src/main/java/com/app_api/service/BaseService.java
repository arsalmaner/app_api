package com.app_api.service;

import com.app_api.resource.entity.AuditInfo;

import java.sql.Timestamp;

public abstract class BaseService {

    public AuditInfo createAudit(Integer userId) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        AuditInfo audit = new AuditInfo();
        audit.setCreatedBy(userId);
        audit.setUpdatedBy(userId);
        audit.setCreatedAt(timestamp);
        audit.setUpdatedAt(timestamp);
        return audit;
    }

    public AuditInfo updateAudit(Integer userId, AuditInfo audit) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        audit.setUpdatedBy(userId);
        audit.setUpdatedAt(timestamp);
        return audit;
    }
}
