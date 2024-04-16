package com.example.ledger.assetPriceTicker;

import com.example.ledger.avro.AssetPriceEventProvided;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
@Slf4j
@EnableScheduling
public class Scheduler {

    private List<Asset> assetList;
    private Map<Long, Asset> assetMap;
    private Random random = new Random();

    @Autowired
    private AssetPriceEventProvidedProducer assetPriceEventProvidedProducer;


    public Scheduler() {

        assetMap = new HashMap<>();
        assetMap.put(1L, new Asset(1, "Euro", 0.94f));
        assetMap.put(2L, new Asset(2, "UK Pound", 0.8f));
        assetMap.put(3L, new Asset(3, "HK Dollar", 7.84f));
        assetMap.put(4L, new Asset(4, "Indian Rupee", 83.61f));
        assetMap.put(5L, new Asset(5, "Japanese Yen", 153.28f));
        assetMap.put(6L, new Asset(6, "Canadian Dollar", 1.38f));

        assetMap.put(7L, new Asset(7, "HSBC", 653.4f));
        assetMap.put(8L, new Asset(8, "LLOY", 50.66f));
        assetMap.put(9L, new Asset(9, "BP", 539.1f));
        assetMap.put(10L, new Asset(10, "Apple Inc", 175.04f));
        assetMap.put(11L, new Asset(11, "BT Group PLC", 105.55f));

        assetMap.put(12L, new Asset(12, "Ethereum", 2619.6f));
        assetMap.put(13L, new Asset(13, "Cardano", 0.4536f));
        assetMap.put(14L, new Asset(14, "Bitcoin", 54266.16f));
        assetMap.put(15L, new Asset(15, "Dogecoin", 0.170105f));
        assetMap.put(16L, new Asset(16, "Litecoin", 85.290f));

        assetMap.put(17L, new Asset(17, "U.S. 10Y", 4.517f));
        assetMap.put(18L, new Asset(18, "U.S. 2Y", 4.623f));
        assetMap.put(19L, new Asset(19, "U.K. 10Y", 4.167f));
        assetMap.put(20L, new Asset(20, "France 10Y", 2.864f));
        assetMap.put(21L, new Asset(21, "Japan 10Y", 0.841f));

    }



    @Scheduled(initialDelay = 5000, fixedRate = 5000)
    public void fixedRateSch() {
        // randomAssetId between [1, 22)
        long randomAssetId = random.nextLong(1, 22);

        updateAssetPrice(randomAssetId);
    }

    private void updateAssetPrice(long assetId) {
        Asset asset = assetMap.get(assetId);

        float originUnitPrice = asset.getUnitPrice();
        // unitPriceChanged between [-0.5, 0.5f)
        float unitPriceChanged = random.nextFloat(-0.5f, 0.5f);
        asset.setUnitPrice(originUnitPrice + unitPriceChanged);

        log.info("PriceTicker:: assetId:{} [{}] priceChanged from {} to {} ::", asset.getAssetId(), asset.getAssetName(), originUnitPrice, asset.getUnitPrice());

        AssetPriceEventProvided assetPriceEventProvided = AssetPriceEventProvided.newBuilder()
                .setAssetId(asset.getAssetId())
                .setAssetName(asset.getAssetName())
                .setUnitPrice(asset.getUnitPrice())
                .build();
        assetPriceEventProvidedProducer.sendMessage(assetPriceEventProvided);
    }

}