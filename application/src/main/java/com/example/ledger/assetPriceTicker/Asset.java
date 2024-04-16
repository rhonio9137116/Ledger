package com.example.ledger.assetPriceTicker;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    private long assetId;
    private String assetName;
    private float unitPrice;
}
