package com.example.ledger.database.repository;


import com.example.ledger.database.entity.AssetBalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AssetBalanceHistoryRepository extends JpaRepository<AssetBalanceHistory, Long> {

    List<AssetBalanceHistory> findByUserIdAndCreatedGreaterThanEqualAndCreatedLessThanEqualOrderByCreatedDesc(Long userId, Date startDate, Date endDate);
}
