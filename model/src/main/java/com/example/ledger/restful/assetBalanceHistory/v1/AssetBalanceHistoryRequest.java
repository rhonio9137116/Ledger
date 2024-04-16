package com.example.ledger.restful.assetBalanceHistory.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetBalanceHistoryRequest {
    @NotNull
    private String requestId;

    @NotNull
    private Long userId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;

}
