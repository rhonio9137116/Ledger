package com.example.ledger.database.repository;


import com.example.ledger.database.entity.WalletAssetOwnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletAssetOwnershipRepository extends JpaRepository<WalletAssetOwnership, Long> {

    List<WalletAssetOwnership> findByAsset_Id(Long assetId);

    List<WalletAssetOwnership> findByWallet_Id(Long walletId);

    Optional<WalletAssetOwnership> findByIdAndWalletIdAndWallet_Account_UserIdAndWallet_Account_AccountStatus(Long walletAssetOwnershipId, Long walletIdFrom, Long userId, String accountStatus);

    Optional<WalletAssetOwnership> findByIdAndWallet_Account_UserIdAndWallet_Account_AccountStatus(Long walletAssetOwnershipId, Long userId, String open);

}
