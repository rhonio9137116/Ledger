package com.example.ledger.restful.asset.v1;

import com.example.ledger.restful.asset.v1.model.WalletView;
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
public class AssetResponse {

    private String responseId;

    private Long userId;

    private List<WalletView> walletViews;

}
