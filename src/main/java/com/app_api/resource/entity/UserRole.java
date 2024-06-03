package com.app_api.resource.entity;

import com.app_api.resource.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_role")
public class UserRole {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "value")
    private UserRoleEnum value;

//    @OneToMany(mappedBy = "role")
//    private Set<User> users = new LinkedHashSet<>();

}