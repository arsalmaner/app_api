package com.app_api.dto.response;

import com.app_api.resource.enums.UserRoleEnum;
import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String name;
    private String email;
    private UserRoleEnum role;
    private Integer employerId;
    private Integer depotId;
    private String phone;
    private Boolean active;

    private String companyName;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zipCode;

    //private List<CountrySellerXrefResponse> countrySellerXrefs;
    //private List<CountryDTO> countryWhser;
}
