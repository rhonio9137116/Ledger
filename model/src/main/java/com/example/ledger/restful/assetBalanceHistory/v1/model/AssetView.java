package com.example.ledger.restful.assetBalanceHistory.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetView {
    private Long assetId;
    private String assetName;
    private List<BalanceHistoryView> balanceHistoryViews;
}
