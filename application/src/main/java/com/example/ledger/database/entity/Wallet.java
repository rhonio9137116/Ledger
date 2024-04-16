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
public class Wallet extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    @Column
    private String walletName;

    @OneToMany(mappedBy = "wallet")
    private List<WalletAssetOwnership> walletAssetOwnerships = new ArrayList<>();

}
