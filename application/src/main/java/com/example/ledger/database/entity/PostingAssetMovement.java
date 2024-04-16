package com.example.ledger.database.entity;

import com.example.ledger.database.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostingAssetMovement extends BaseEntity {
    @Column
    private Long walletAssetOwnershipId;

    @Column
    private Long walletIdFrom;

    @Column
    private Long walletIdTo;

    @ManyToOne
    @JoinColumn(name = "postingId")
    private Posting posting;
}
