package com.example.ledger.database.repository;

import com.example.ledger.database.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {
    Optional<Posting> findByIdAndUserId(long postingId, long userId);
}
