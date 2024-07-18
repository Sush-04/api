package com.example.ApiGen.repo;

import com.example.ApiGen.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByApiKeyHash(String apiKeyHash);
}
