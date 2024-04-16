package com.example.ledger.restful.postingReplay.v1;


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
public class PostingReplayRequest {

    @NotNull
    private String requestId;

    @NotNull
    private Long userId;

    @NotNull
    private Long postingId;
}
