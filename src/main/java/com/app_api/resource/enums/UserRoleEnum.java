package com.app_api.resource.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum UserRoleEnum {

    admin(Set.of(
            Permission.ADMIN)),
    seller(Set.of(
            Permission.SELLER)),
    sellerEmp(Set.of(
            Permission.SELLEREMP)),
    whser(Set.of(
            Permission.WHSER)),
    whserEmp(Set.of(
            Permission.WHSEREMP));

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
