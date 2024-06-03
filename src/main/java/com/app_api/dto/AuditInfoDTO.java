package com.app_api.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AuditInfoDTO {
    private Integer createdBy;
    private Integer updatedBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
