package com.example.ledger.api.controller.asset.v1;


import com.example.ledger.api.service.AssetService;
import com.example.ledger.restful.asset.v1.AssetRequest;
import com.example.ledger.restful.asset.v1.AssetResponse;
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
public class AssetController {

    @Autowired
    private AssetService assetService;


    @Operation(summary = "get current wallets and assets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success return the current wallets and assets",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AssetRequest.class))})})
    @PostMapping(value = "/list/v1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetResponse getCurrentAssets(@Valid @RequestBody AssetRequest assetRequest) throws GenericRestfulException {
        log.info("POST /asset/list/v1 {}" + assetRequest);

        AssetResponse response = assetService.getCurrentAssets(assetRequest);

        return response;
    }
}