package com.example.ledger.api.controller.account.v1;


import com.example.ledger.api.service.AccountService;
import com.example.ledger.restful.account.v1.AccountLifecycleRequest;
import com.example.ledger.restful.account.v1.AccountLifecycleResponse;
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
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "lifecycle of account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success lifecycle of account",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountLifecycleRequest.class))})})
    @PostMapping(value = "/lifecycle/v1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountLifecycleResponse accountLifecycle(@Valid @RequestBody AccountLifecycleRequest request) throws GenericRestfulException {
        log.info("POST /account/lifecycle/v1 {}" + request);

        AccountLifecycleResponse response = accountService.accountLifecycle(request);
        return response;
    }
}