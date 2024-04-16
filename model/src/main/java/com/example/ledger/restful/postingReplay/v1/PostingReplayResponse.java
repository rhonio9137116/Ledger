package com.example.ledger.restful.postingReplay.v1;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostingReplayResponse {

    private String responseId;

    private Long postingId;

    @Pattern(regexp = "^(OPEN|CLEARED|FAILED)$")
    private String postingStatus;
}
