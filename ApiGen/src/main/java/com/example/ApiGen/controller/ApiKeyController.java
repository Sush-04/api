package com.example.ApiGen.controller;

import com.example.ApiGen.entity.TradeData;
import com.example.ApiGen.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ApiKeyController {

    @Autowired
    private ApiKeyService apiKeyService;

    @PostMapping("/generateApiKey")
    public ResponseEntity<String> generateApiKey(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        String name = payload.get("name").toString();
        String apiKey = apiKeyService.generateApiKey(userId, name);
        return ResponseEntity.ok(apiKey);
    }

    @GetMapping("/validateApiKey")
    public ResponseEntity<String> validateApiKey(@RequestParam String apiKey) {
        boolean isValid = apiKeyService.validateApiKey(apiKey);
        return isValid ? ResponseEntity.ok("API key is valid") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("API key is invalid");
    }

    @GetMapping("/access")
    public ResponseEntity<?> checkAccess(@RequestParam String apiKey, @RequestParam Set<String> requestedAccess) {
        List<TradeData> tradeData = apiKeyService.getAccessibleTradeData(apiKey, requestedAccess);
        if (!tradeData.isEmpty()) {
            System.out.println("enters");
            return ResponseEntity.ok(tradeData);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Retry");
    }
}
