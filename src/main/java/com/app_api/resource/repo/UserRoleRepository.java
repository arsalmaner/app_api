package com.app_api.resource.repo;

import com.app_api.resource.entity.UserRole;
import com.app_api.resource.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    Optional<UserRole> findByValue(UserRoleEnum e);
}