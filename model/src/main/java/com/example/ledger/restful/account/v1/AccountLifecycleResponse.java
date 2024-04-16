package com.example.ledger.restful.account.v1;

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
public class AccountLifecycleResponse {
    private String responseId;
    private Long accountId;
    private Long userId;
    @Pattern(regexp = "^(OPEN|CLEARED|FAILED)$")
    private String status;
}
