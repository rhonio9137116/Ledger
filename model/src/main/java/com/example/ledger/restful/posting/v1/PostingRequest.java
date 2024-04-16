package com.example.ledger.restful.posting.v1;


import com.example.ledger.restful.posting.v1.model.AssetMovement;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class PostingRequest {

    @NotNull
    private String requestId;

    @NotNull
    private Long userId;

    @NotEmpty
    private List<AssetMovement> assetMovements;
}
