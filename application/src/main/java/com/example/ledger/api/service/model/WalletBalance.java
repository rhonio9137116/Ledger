package com.example.ledger.api.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletBalance {
    private long walletId;
    private String walletName;
    private long userId;
    private double walletBalance;
}
