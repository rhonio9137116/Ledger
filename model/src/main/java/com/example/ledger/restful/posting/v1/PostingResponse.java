package com.example.ledger.restful.posting.v1;


import com.example.ledger.restful.posting.v1.model.AssetMovement;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
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
public class PostingResponse {

    private String responseId;

    private Long postingId;

    private List<AssetMovement> assetMovements;

    @Pattern(regexp = "^(OPEN|CLEARED|FAILED)$")
    private String postingStatus;
}
