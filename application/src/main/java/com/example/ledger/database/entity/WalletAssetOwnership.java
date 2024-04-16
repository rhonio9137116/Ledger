package com.example.ledger.database.entity;

import com.example.ledger.database.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class WalletAssetOwnership extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "walletId")
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "assetId")
    private Asset asset;

    @Column
    private Float units;

    @OneToMany(mappedBy = "walletAssetOwnership")
    private List<AssetBalanceHistory> assetBalanceHistories = new ArrayList<>();
}
