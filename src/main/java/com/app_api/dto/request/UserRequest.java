package com.app_api.dto.request;

import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String phone;

    private String companyName;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zipCode;
}
