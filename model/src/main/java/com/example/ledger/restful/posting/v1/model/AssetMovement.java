package com.example.ledger.restful.posting.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetMovement {

    @NotNull
    private Long walletAssetOwnershipId;
    @NotNull
    private Long walletIdFrom;
    @NotNull
    private Long walletIdTo;


}
