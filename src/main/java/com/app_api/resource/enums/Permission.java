package com.app_api.resource.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN("admin"),

    SELLER("seller"),

    SELLEREMP("sellerEmp"),

    WHSER("whser"),

    WHSEREMP("whserEmp");

    @Getter
    private final String permission;
}
