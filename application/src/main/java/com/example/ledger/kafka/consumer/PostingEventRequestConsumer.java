package com.example.ledger.kafka.consumer;

import com.example.ledger.api.service.AssetService;
import com.example.ledger.avro.PostingEventProvided;
import com.example.ledger.avro.PostingEventRequest;
import com.example.ledger.database.entity.Posting;
import com.example.ledger.database.repository.PostingRepository;
import com.example.ledger.kafka.producer.PostingEventProvidedProducer;
import com.example.ledger.restful.exception.GenericRestfulException;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@CommonsLog(topic = "PostingEventRequestConsumer")
public class PostingEventRequestConsumer {

    @Value("${topic.postingEventRequest.name}")
    private String topic;

    @Autowired
    private AssetService assetService;

    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private PostingEventProvidedProducer postingEventProvidedProducer;

    @KafkaListener(topics = "postingEventRequestTopic", groupId = "group_id")
    public void consume(ConsumerRecord<String, PostingEventRequest> record) throws GenericRestfulException {

        PostingEventRequest postingEventRequest = record.value();
        log.info(String.format("Consumed PostingEventRequest from Topic:%s-> %s", topic, postingEventRequest));

        Long userId = postingEventRequest.getUserId();
        Long postingId = postingEventRequest.getPostingId();

        try {
            if (assetService.postingAssetsFromOneWalletToAnotherWallet(postingId, userId)) {
                updatePostingStatus(postingId, Posting.STATUS_CLEARED);

                // publish CLEARED to postingEventProvided Topic
                PostingEventProvided postingEventProvided = PostingEventProvided.newBuilder()
                        .setPostingId(postingEventRequest.getPostingId())
                        .setStatus(Posting.STATUS_CLEARED)
                        .setUserId(userId)
                        .build();
                postingEventProvidedProducer.sendMessage(postingEventProvided);

            } else {
                updatePostingStatus(postingId, Posting.STATUS_FAILED);
                // publish FAILED to postingEventProvided Topic
                PostingEventProvided postingEventProvided = PostingEventProvided.newBuilder()
                        .setPostingId(postingEventRequest.getPostingId())
                        .setStatus(Posting.STATUS_FAILED)
                        .setUserId(userId)
                        .build();
                postingEventProvidedProducer.sendMessage(postingEventProvided);
            }
        } catch (Exception e) {
            updatePostingStatus(postingId, Posting.STATUS_FAILED);

            throw new GenericRestfulException(e.getMessage());
        }
    }

    private void updatePostingStatus(Long postingId, String postingStatus) {
        Optional<Posting> postingOptional = postingRepository.findById(postingId);
        if (postingOptional.isPresent()) {
            postingOptional.get().setStatus(postingStatus);
            postingRepository.save(postingOptional.get());
            log.info(postingStatus + " PostingId:" + postingId);
        }
    }

}
