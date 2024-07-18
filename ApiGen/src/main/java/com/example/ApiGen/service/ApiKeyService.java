package com.example.ApiGen.service;

import com.example.ApiGen.entity.ApiKey;
import com.example.ApiGen.entity.RoleRes;
import com.example.ApiGen.entity.TradeData;
import com.example.ApiGen.entity.UserRole;
import com.example.ApiGen.repo.ApiKeyRepository;
import com.example.ApiGen.repo.RoleResRepository;
import com.example.ApiGen.repo.TradeDataRepository;
import com.example.ApiGen.repo.UserRoleRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApiKeyService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleResRepository roleResRepository;

    @Autowired
    private TradeDataRepository tradeDataRepository;

    public String generateApiKey(Long userId, String name) {
        String rawApiKey = UUID.randomUUID().toString();
        String hashedApiKey = DigestUtils.sha256Hex(rawApiKey);
        System.out.println(rawApiKey);
        System.out.println(hashedApiKey);

        ApiKey apiKey = new ApiKey();
        apiKey.setUserId(userId);
        apiKey.setName(name);
        apiKey.setApiKeyHash(hashedApiKey);

        apiKeyRepository.save(apiKey);

        return rawApiKey;
    }

    public boolean validateApiKey(String rawApiKey) {
        String hashedApiKey = DigestUtils.sha256Hex(rawApiKey);
        return apiKeyRepository.findByApiKeyHash(hashedApiKey).isPresent();
    }

//    public List<TradeData> getAccessibleTradeData(String rawApiKey, Set<String> requestedAccess) {
//        System.out.println("Starting getAccessibleTradeData");
//        String hashedApiKey = DigestUtils.sha256Hex(rawApiKey);
//        System.out.println("the hashedApiKey is " + hashedApiKey);
//        Optional<ApiKey> apiKeyOpt = apiKeyRepository.findByApiKeyHash(hashedApiKey);
//
//        if (apiKeyOpt.isPresent()) {
//            Long userId = apiKeyOpt.get().getUserId();
//
//            Optional<UserRole> userRoleOpt = userRoleRepository.findByUserId(userId);
//
//            if (userRoleOpt.isPresent()) {
//                Long roleId = userRoleOpt.get().getRoleId();
//                Optional<RoleRes> roleResOpt = roleResRepository.findById(roleId);
//
//                if (roleResOpt.isPresent()) {
//                    Set<String> access = roleResOpt.get().getAccess();
//                    if (access.containsAll(requestedAccess)) {
//                        return tradeDataRepository.findByAssetTypeIn(access);
//                    }
//                }
//            }
//        }
//        return Collections.emptyList();
//    }
public List<TradeData> getAccessibleTradeData(String apiKey, Set<String> requestedAccess) {
    String hashedApiKey = DigestUtils.sha256Hex(apiKey);
    Optional<ApiKey> apiKeyEntityOptional = apiKeyRepository.findByApiKeyHash(hashedApiKey);
    if (apiKeyEntityOptional.isEmpty()) {
        return Collections.emptyList();
    }

    Long userId = apiKeyEntityOptional.get().getUserId();
    System.out.println("User ID: " + userId);

    Optional<UserRole> userRoleOptional = userRoleRepository.findByUserId(userId);
    if (userRoleOptional.isEmpty()) {
        return Collections.emptyList();
    }

    UserRole userRole = userRoleOptional.get();
    Long roleId = userRole.getRoleId();
    System.out.println("Role ID: " + roleId);

    Optional<RoleRes> roleOptional = roleResRepository.findById(roleId);
    if (roleOptional.isEmpty()) {
        return Collections.emptyList();
    }
    RoleRes role = roleOptional.get();

    Set<String> userAccess = role.getAccess().stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    System.out.println("User Access: " + userAccess);

    Set<String> normalizedRequestedAccess = requestedAccess.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());

    if (!userAccess.containsAll(normalizedRequestedAccess)) {
        System.out.println("Requested access not found in user access");
        return Collections.emptyList();
    }

    // Fetch trade data that matches the requested access (case insensitive)
    List<TradeData> tradeData = tradeDataRepository.findByAssetTypeInIgnoreCase(new ArrayList<>(normalizedRequestedAccess));

    return tradeData;
}


}
