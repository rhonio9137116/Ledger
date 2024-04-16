package com.example.ledger.restful.assetBalanceHistory.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BalanceHistoryView {

    private float units;

    private float unitPrice;

    private double assetBalance;

    private String timestamp;
}
