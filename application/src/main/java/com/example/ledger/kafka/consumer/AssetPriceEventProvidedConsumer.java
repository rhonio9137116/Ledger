package com.example.ledger.kafka.consumer;

import com.example.ledger.api.service.AssetService;
import com.example.ledger.api.service.model.WalletBalance;
import com.example.ledger.avro.AssetPriceEventProvided;
import com.example.ledger.avro.WalletBalanceEventProvided;
import com.example.ledger.kafka.producer.WalletBalanceEventProvidedProducer;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@CommonsLog(topic = "AssetPriceEventProvidedConsumer")
public class AssetPriceEventProvidedConsumer {

    @Value("${topic.assetPriceEventProvided.name}")
    private String topic;

    @Autowired
    private WalletBalanceEventProvidedProducer walletBalanceEventProvidedProducer;

    @Autowired
    private AssetService assetService;


    @KafkaListener(topics = "assetPriceEventProvidedTopic", groupId = "group_id2")
    public void consume(ConsumerRecord<String, AssetPriceEventProvided> record) {

        AssetPriceEventProvided assetPriceEventProvided = record.value();
        log.info(String.format("Consumed assetPriceEventProvided from Topic:%s-> %s", topic, assetPriceEventProvided));

        Long assetId = assetPriceEventProvided.getAssetId();
        Float unitPrice = assetPriceEventProvided.getUnitPrice();

        // [Notify to client subscriber for Wallet Balance Changed]
        // notify wallet balance update is real time process, do this in the beginning of the method
        // get all wallets that has balance changed
        List<WalletBalance> walletsBalanceChanged = assetService.getWalletsBalanceChanged(assetId, unitPrice);
        // loop though each wallet that has balance changed
        for (WalletBalance walletBalance : walletsBalanceChanged) {
            // push to walletBalanceEvent to  walletBalanceEventTopic
            WalletBalanceEventProvided walletBalanceEventProvided = WalletBalanceEventProvided.newBuilder()
                    .setWalletId(walletBalance.getWalletId())
                    .setWalletName(walletBalance.getWalletName())
                    .setUserId(walletBalance.getUserId())
                    .setBalance(walletBalance.getWalletBalance())
                    .build();
            walletBalanceEventProvidedProducer.sendMessage(walletBalanceEventProvided);
        }


        // [Record wallet asset Balance History]
        // retrieve asset Balance History is not frequently done by user, so do this at the end of the method
        assetService.recordWalletAssetBalanceHistory(assetId, unitPrice);


    }

}
