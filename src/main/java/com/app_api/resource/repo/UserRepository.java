package com.app_api.resource.repo;

import com.app_api.resource.entity.User;
import com.app_api.resource.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    List<User> findByDepotAndRole(User depot, UserRole role);
}