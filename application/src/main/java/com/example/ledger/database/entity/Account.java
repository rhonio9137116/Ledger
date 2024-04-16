package com.example.ledger.database.entity;

import com.example.ledger.database.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class Account extends BaseEntity {

    public static String STATUS_OPEN = "OPEN";
    public static String STATUS_CLOSED = "CLOSED";

    @Column
    private Long userId;

    @Column
    private String accountName;

    @Column
    private String accountStatus;

    @OneToMany(mappedBy = "account")
    private List<Wallet> wallets = new ArrayList<>();

}

