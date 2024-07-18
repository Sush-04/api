package com.example.ApiGen.repo;

import com.example.ApiGen.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByUserId(Long userId);
}