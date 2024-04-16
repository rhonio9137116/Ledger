package com.example.ledger.database.repository;

import com.example.ledger.database.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByIdAndAccount_UserIdAndAccount_AccountStatus(Long walletId, Long userId, String accountStatus);

    List<Wallet> findByAccount_UserId(Long userId);
}

