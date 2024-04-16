package com.example.ledger.api.controller.assetPostingReplay.v1;


import com.example.ledger.api.service.AssetService;
import com.example.ledger.avro.PostingEventRequest;
import com.example.ledger.kafka.producer.PostingEventRequestProducer;
import com.example.ledger.restful.exception.GenericRestfulException;
import com.example.ledger.restful.postingReplay.v1.PostingReplayRequest;
import com.example.ledger.restful.postingReplay.v1.PostingReplayResponse;
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
public class PostingReplayController {
    @Autowired
    private PostingEventRequestProducer postingEventRequestProducer;

    @Autowired
    private AssetService assetService;


    @Operation(summary = "Replay existing Posting, Assets movement from one wallet to another wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success Replay existing Posting, Assets movement from one wallet to another wallet",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostingReplayRequest.class))})})
    @PostMapping(value = "/posting/replay/v1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostingReplayResponse postingReplay(@Valid @RequestBody PostingReplayRequest postingReplayRequest) throws GenericRestfulException {

        PostingReplayResponse postingReplayResponse = assetService.postingReplyRequested(postingReplayRequest);

        //  MQ publish to postingEventRequest Topic
        PostingEventRequest postingEventRequest = PostingEventRequest.newBuilder()
                .setPostingId(postingReplayResponse.getPostingId())
                .setUserId(postingReplayRequest.getUserId())
                .build();
        this.postingEventRequestProducer.sendMessage(postingEventRequest);

        return postingReplayResponse;

    }
}