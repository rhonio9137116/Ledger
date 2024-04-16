package com.example.ledger.api.controller.assetBalanceHistory.v1;


import com.example.ledger.api.service.AssetService;
import com.example.ledger.restful.assetBalanceHistory.v1.AssetBalanceHistoryRequest;
import com.example.ledger.restful.assetBalanceHistory.v1.AssetBalanceHistoryResponse;
import com.example.ledger.restful.exception.GenericRestfulException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/asset")
public class AssetBalanceHistoryController {

    @Autowired
    private AssetService assetService;


    @Operation(summary = "retrieve asset balance history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success retrieve asset balance history",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AssetBalanceHistoryRequest.class))})})
    @PostMapping(value = "/balance/history/v1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetBalanceHistoryResponse assetBalanceHistory(@Valid @RequestBody AssetBalanceHistoryRequest request) throws GenericRestfulException {
        log.info("POST /asset/balance/history/v1 {}" + request);

        AssetBalanceHistoryResponse response = assetService.getAssetBalanceHistories(request);

        return response;
    }
}