package com.example.ledger.restful.account.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
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
public class AccountLifecycleRequest {
    @NotNull
    private String requestId;

    @NotNull
    private Long accountId;

    @NotNull
    private Long userId;

    @NotNull
    @Pattern(regexp = "^(OPEN|CLOSED)$")
    private String status;
}
