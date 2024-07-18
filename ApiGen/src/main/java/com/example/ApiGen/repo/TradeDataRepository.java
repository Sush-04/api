package com.example.ApiGen.repo;

import com.example.ApiGen.entity.TradeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TradeDataRepository extends JpaRepository<TradeData, Long> {
    @Query(value = "SELECT * FROM trade_data WHERE LOWER(asset_type) IN (:assetTypes)", nativeQuery = true)
    List<TradeData> findByAssetTypeInIgnoreCase(List<String> assetTypes);
//    List<TradeData> findByAssetTypeIn(Set<String> assetTypes);
}
