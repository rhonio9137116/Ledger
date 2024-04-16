package com.example.ledger.database.repository;

import com.example.ledger.database.entity.PostingAssetMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostingAssetMovementRepository extends JpaRepository<PostingAssetMovement, Long> {

    Optional<PostingAssetMovement> findByWalletIdFrom(Long walletId);
    List<PostingAssetMovement> findByPostingIdAndPosting_UserId(long postingId, long userId);

}
