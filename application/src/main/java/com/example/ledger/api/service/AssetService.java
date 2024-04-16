package com.example.ledger.api.service;

import com.example.ledger.api.service.model.WalletBalance;
import com.example.ledger.restful.asset.v1.AssetRequest;
import com.example.ledger.restful.asset.v1.AssetResponse;
import com.example.ledger.restful.assetBalanceHistory.v1.AssetBalanceHistoryRequest;
import com.example.ledger.restful.assetBalanceHistory.v1.AssetBalanceHistoryResponse;
import com.example.ledger.restful.exception.GenericRestfulException;
import com.example.ledger.restful.posting.v1.PostingRequest;
import com.example.ledger.restful.posting.v1.PostingResponse;
import com.example.ledger.restful.postingReplay.v1.PostingReplayRequest;
import com.example.ledger.restful.postingReplay.v1.PostingReplayResponse;

import java.util.List;

public interface AssetService {
    List<WalletBalance> getWalletsBalanceChanged(long assetId, float assetPriceChanged);

    void recordWalletAssetBalanceHistory(long assetId, Float unitPrice);

    AssetBalanceHistoryResponse getAssetBalanceHistories(AssetBalanceHistoryRequest request);

    PostingResponse postingAssetsFromOneWalletToAnotherWalletRequested(PostingRequest postingRequest) throws GenericRestfulException;

    PostingReplayResponse postingReplyRequested(PostingReplayRequest postingReplayRequest) throws GenericRestfulException;

    // return true to clear the posting, false to fail the positng
    boolean postingAssetsFromOneWalletToAnotherWallet(long postingId, long userId) throws GenericRestfulException;

    AssetResponse getCurrentAssets(AssetRequest assetRequest);
}
