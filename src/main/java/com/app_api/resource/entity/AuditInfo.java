package com.app_api.resource.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

@Getter
@Setter
@Embeddable
public class AuditInfo {

    @Column(name = "created_by")
    @CreatedBy
    private Integer createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    private Integer updatedBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    @CreatedDate
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false)
    @LastModifiedDate
    private Timestamp updatedAt;

}
