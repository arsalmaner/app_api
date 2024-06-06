package com.app_api.service;

import com.app_api.resource.entity.AuditInfo;
import com.app_api.resource.entity.User;
import com.app_api.util.CurrentUserHolder;

import java.sql.Timestamp;

public class BaseService {

    protected User currentUser;

    public BaseService(CurrentUserHolder currentUserHolder) {
        currentUser = currentUserHolder.getCurrentUser();
    }

    public AuditInfo createAudit() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        AuditInfo audit = new AuditInfo();
        audit.setCreatedBy(currentUser.getId());
        audit.setUpdatedBy(currentUser.getId());
        audit.setCreatedAt(timestamp);
        audit.setUpdatedAt(timestamp);
        return audit;
    }

    public AuditInfo updateAudit(AuditInfo audit) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        audit.setUpdatedBy(currentUser.getId());
        audit.setUpdatedAt(timestamp);
        return audit;
    }
}
