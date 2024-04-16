package com.example.ledger.api.controller.assetPosting.v1;


import com.example.ledger.api.service.AssetService;
import com.example.ledger.avro.PostingEventRequest;
import com.example.ledger.kafka.producer.PostingEventRequestProducer;
import com.example.ledger.restful.exception.GenericRestfulException;
import com.example.ledger.restful.posting.v1.PostingRequest;
import com.example.ledger.restful.posting.v1.PostingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/asset")
public class PostingController {
    @Autowired
    private PostingEventRequestProducer postingEventRequestProducer;

    @Autowired
    private AssetService assetService;

    @Operation(summary = "Posting, Assets movement from one wallet to another wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success Posting, Assets movement from one wallet to another wallet",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostingRequest.class))})})
    @PostMapping(value = "/posting/v1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostingResponse postingAssetsFromOneWalletToAnotherWallet(@Valid @RequestBody PostingRequest postingRequest) throws GenericRestfulException {

        PostingResponse postingResponse = assetService.postingAssetsFromOneWalletToAnotherWalletRequested(postingRequest);

        //  MQ publish to postingEventRequest Topic
        PostingEventRequest postingEventRequest = PostingEventRequest.newBuilder()
                .setPostingId(postingResponse.getPostingId())
                .setUserId(postingRequest.getUserId())
                .build();
        this.postingEventRequestProducer.sendMessage(postingEventRequest);

        return postingResponse;

    }

}