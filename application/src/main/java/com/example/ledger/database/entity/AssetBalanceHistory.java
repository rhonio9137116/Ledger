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
public class AssetBalanceHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "assetId")
    private Asset asset;

    @Column
    private Float units;

    @Column
    private Float unitPrice;

    @Column
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "walletAssetOwnershipId")
    private WalletAssetOwnership walletAssetOwnership;

}
