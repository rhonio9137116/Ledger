package com.example.ledger.api.service;


import com.example.ledger.api.service.model.WalletBalance;
import com.example.ledger.database.entity.*;
import com.example.ledger.database.repository.*;
import com.example.ledger.restful.asset.v1.AssetRequest;
import com.example.ledger.restful.asset.v1.AssetResponse;
import com.example.ledger.restful.asset.v1.model.AssetView;
import com.example.ledger.restful.asset.v1.model.WalletView;
import com.example.ledger.restful.assetBalanceHistory.v1.AssetBalanceHistoryRequest;
import com.example.ledger.restful.assetBalanceHistory.v1.AssetBalanceHistoryResponse;
import com.example.ledger.restful.assetBalanceHistory.v1.model.BalanceHistoryView;
import com.example.ledger.restful.exception.GenericRestfulException;
import com.example.ledger.restful.posting.v1.PostingRequest;
import com.example.ledger.restful.posting.v1.PostingResponse;
import com.example.ledger.restful.posting.v1.model.AssetMovement;
import com.example.ledger.restful.postingReplay.v1.PostingReplayRequest;
import com.example.ledger.restful.postingReplay.v1.PostingReplayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = {Exception.class})
@Slf4j
public class AssetServiceImpl implements AssetService {
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletAssetOwnershipRepository walletAssetOwnershipRepository;

    @Autowired
    private AssetBalanceHistoryRepository assetBalanceHistoryRepository;

    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private PostingAssetMovementRepository postingAssetMovementRepository;

    @Override
    public List<WalletBalance> getWalletsBalanceChanged(long assetId, float assetPriceChanged) {
        List<WalletBalance> walletBalances = new ArrayList<>();

        Optional<Asset> assetOptional = assetRepository.findById(assetId);
        if (assetOptional.isPresent()) {
            // find all wallet asset ownership that contains this asset
            List<WalletAssetOwnership> walletAssetOwnerships = walletAssetOwnershipRepository.findByAsset_Id(assetId);
            for (WalletAssetOwnership walletAssetOwnership : walletAssetOwnerships) {

                //  find all assets of the wallet
                Wallet wallet = walletRepository.findById(walletAssetOwnership.getWallet().getId()).get();

                List<WalletAssetOwnership> walletAssetOwnershipsPerWallet = wallet.getWalletAssetOwnerships();

                // calculate the Balance SUM of the wallet
                double walletBalanceSum = 0;
                for (WalletAssetOwnership ownershipOfWallet : walletAssetOwnershipsPerWallet) {
                    walletBalanceSum += ownershipOfWallet.getUnits() * assetPriceChanged;
                }

                log.info("Balance Changed for userId:{} wallet:{}, assets size:{}, Total Balance:${}", wallet.getAccount().getUserId(), wallet.getWalletName(), walletAssetOwnershipsPerWallet.size(), walletBalanceSum);
                WalletBalance walletBalance = WalletBalance.builder()
                        .walletId(wallet.getId())
                        .walletName(wallet.getWalletName())
                        .userId(wallet.getAccount().getUserId())
                        .walletBalance(walletBalanceSum)
                        .build();
                walletBalances.add(walletBalance);
            }
        }


        return walletBalances;
    }

    @Override
    public void recordWalletAssetBalanceHistory(long assetId, Float unitPrice) {
        Optional<Asset> assetOptional = assetRepository.findById(assetId);
        if (assetOptional.isPresent()) {
            Asset asset = assetOptional.get();

            // find all assetOwnerships, loop though each and save to assetBalanceHistory
            List<WalletAssetOwnership> walletAssetOwnerships = walletAssetOwnershipRepository.findByAsset_Id(assetId);
            for (WalletAssetOwnership walletAssetOwnership : walletAssetOwnerships) {
                AssetBalanceHistory assetBalanceHistory = new AssetBalanceHistory();
                assetBalanceHistory.setAsset(asset);
                assetBalanceHistory.setUserId(walletAssetOwnership.getWallet().getAccount().getUserId());
                assetBalanceHistory.setWalletAssetOwnership(walletAssetOwnership);
                assetBalanceHistory.setUnits(walletAssetOwnership.getUnits());
                assetBalanceHistory.setUnitPrice(unitPrice);

                assetBalanceHistoryRepository.save(assetBalanceHistory);
                log.info("Record WalletAssetBalanceHistory, userId:{} walletName:{} assetName:{} units:{} balance:${}", assetBalanceHistory.getUserId(), assetBalanceHistory.getWalletAssetOwnership().getWallet().getWalletName(), assetBalanceHistory.getAsset().getAssetName(), assetBalanceHistory.getUnits()
                        , assetBalanceHistory.getUnits() * assetBalanceHistory.getUnitPrice());
            }
        }
    }

    @Override
    public AssetBalanceHistoryResponse getAssetBalanceHistories(AssetBalanceHistoryRequest request) {
        // use a map of map to store wallet -> asset -> AssetBalanceHistories structure
        // Map[walletId, Map[assetId,List<AssetBalanceHistories>]]
        Map<Long, Map<Long, List<AssetBalanceHistory>>> walletIdAssetIdAssetBalanceHistoryMap = new HashMap<>();
        List<AssetBalanceHistory> AssetBalanceHistories = assetBalanceHistoryRepository.findByUserIdAndCreatedGreaterThanEqualAndCreatedLessThanEqualOrderByCreatedDesc(request.getUserId(), request.getStartDate(), request.getEndDate());
        for (AssetBalanceHistory assetBalanceHistory : AssetBalanceHistories) {
            Long walletId = assetBalanceHistory.getWalletAssetOwnership().getWallet().getId();
            Long assetId = assetBalanceHistory.getAsset().getId();

            // Map[assetId,List<AssetBalanceHistories>]]
            Map<Long, List<AssetBalanceHistory>> assetIdAssetBalanceHistoryMap = walletIdAssetIdAssetBalanceHistoryMap.get(walletId);
            if (assetIdAssetBalanceHistoryMap == null) {
                assetIdAssetBalanceHistoryMap = new HashMap<>();
                walletIdAssetIdAssetBalanceHistoryMap.put(walletId, assetIdAssetBalanceHistoryMap);

            }

            List<AssetBalanceHistory> assetBalanceHistories = assetIdAssetBalanceHistoryMap.get(assetId);
            if(assetBalanceHistories ==null){
                assetBalanceHistories = new ArrayList<>();
                assetIdAssetBalanceHistoryMap.put(assetId, assetBalanceHistories);
            }
            assetBalanceHistories.add(assetBalanceHistory);

        }

        // loop from the map of map [wallet -> asset -> AssetBalanceHistories structure] to construct the Restful Response Views
        List<com.example.ledger.restful.assetBalanceHistory.v1.model.WalletView> walletViews = walletIdAssetIdAssetBalanceHistoryMap.entrySet().stream().map(mapEntryOfWalletId -> {
                    Long walletId = mapEntryOfWalletId.getKey();
                    Wallet wallet = walletRepository.findById(walletId).get();

                    Map<Long, List<AssetBalanceHistory>> assetIdAssetBalanceHistoryMap = mapEntryOfWalletId.getValue();

                    List<com.example.ledger.restful.assetBalanceHistory.v1.model.AssetView> assetViews = assetIdAssetBalanceHistoryMap.entrySet().stream().map(mapEntryOfAssetId -> {
                                Long assetId = mapEntryOfAssetId.getKey();
                                Asset asset = assetRepository.findById(assetId).get();
                                List<AssetBalanceHistory> assetBalanceHistories = mapEntryOfAssetId.getValue();

                                List<BalanceHistoryView> balanceHistoryViews = assetBalanceHistories.stream().map(assetBalanceHistory -> {
                                            BalanceHistoryView balanceHistoryView = BalanceHistoryView.builder()
                                                    .units(assetBalanceHistory.getUnits())
                                                    .unitPrice(assetBalanceHistory.getUnitPrice())
                                                    .assetBalance(assetBalanceHistory.getUnits() * assetBalanceHistory.getUnitPrice())
                                                    .timestamp(assetBalanceHistory.getCreated().toString())
                                                    .build();
                                            return balanceHistoryView;
                                        }
                                ).collect(Collectors.toList());


                                com.example.ledger.restful.assetBalanceHistory.v1.model.AssetView assetView = com.example.ledger.restful.assetBalanceHistory.v1.model.AssetView.builder()
                                        .assetId(assetId)
                                        .assetName(asset.getAssetName())
                                        .balanceHistoryViews(balanceHistoryViews)
                                        .build();
                                return assetView;
                            }
                    ).collect(Collectors.toList());


                    com.example.ledger.restful.assetBalanceHistory.v1.model.WalletView walletView = com.example.ledger.restful.assetBalanceHistory.v1.model.WalletView.builder()
                            .walletId(wallet.getId())
                            .walletName(wallet.getWalletName())
                            .assetViews(assetViews)
                            .build();

                    return walletView;
                })
                .collect(Collectors.toList());

        AssetBalanceHistoryResponse assetBalanceHistoryResponse = AssetBalanceHistoryResponse.builder()
                .responseId(request.getRequestId())
                .userId(request.getUserId())
                .walletViews(walletViews)
                .build();

        return assetBalanceHistoryResponse;
    }

    @Override
    public PostingResponse postingAssetsFromOneWalletToAnotherWalletRequested(PostingRequest postingRequest) throws GenericRestfulException {
        Long userId = postingRequest.getUserId();

        Posting posting = new Posting();
        posting.setDescription("Move Assets");
        posting.setUserId(userId);
        posting.setStatus(Posting.STATUS_PENDING);

        // each Posting support multiple Asset Movements
        List<AssetMovement> assetMovements = postingRequest.getAssetMovements();
        for (AssetMovement assetMovement : assetMovements) {
            // For each Asset Movement

            Optional<WalletAssetOwnership> walletAssetOwnershipOptional = walletAssetOwnershipRepository.findByIdAndWallet_Account_UserIdAndWallet_Account_AccountStatus(assetMovement.getWalletAssetOwnershipId(), userId, Account.STATUS_OPEN);
            if (walletAssetOwnershipOptional.isPresent()) {
                // the asset belongs to Open Account of the user

                Optional<Wallet> walletFromOptional = walletRepository.findByIdAndAccount_UserIdAndAccount_AccountStatus(assetMovement.getWalletIdFrom(), userId, Account.STATUS_OPEN);
                Optional<Wallet> walletToOptional = walletRepository.findByIdAndAccount_UserIdAndAccount_AccountStatus(assetMovement.getWalletIdTo(), userId, Account.STATUS_OPEN);
                if (walletFromOptional.isPresent() && walletToOptional.isPresent()) {
                    // the walletFrom and walletTo belongs to Open Account of the user

                    PostingAssetMovement postingAssetMovement = new PostingAssetMovement();
                    postingAssetMovement.setWalletAssetOwnershipId(assetMovement.getWalletAssetOwnershipId());
                    postingAssetMovement.setWalletIdFrom(assetMovement.getWalletIdFrom());
                    postingAssetMovement.setWalletIdTo(assetMovement.getWalletIdTo());
                    postingAssetMovement.setPosting(posting);
                    postingAssetMovementRepository.save(postingAssetMovement);
                    posting = postingRepository.save(posting);

                } else {
                    // transaction roll back
                    throw new GenericRestfulException("the walletFrom Or walletTo do not belongs to Open Account of the user");
                }
            } else {
                // transaction roll back
                throw new GenericRestfulException("the asset do not belongs to Open Account of the user");
            }

        }

        if (posting.getId() != null) {
            return PostingResponse.builder()
                    .responseId(postingRequest.getRequestId())
                    .postingId(posting.getId())
                    .assetMovements(postingRequest.getAssetMovements())
                    .postingStatus(posting.getStatus())
                    .build();
        } else {
            throw new GenericRestfulException("Server unable to create Posting");
        }

    }


    @Override
    public PostingReplayResponse postingReplyRequested(PostingReplayRequest postingReplayRequest) throws GenericRestfulException {
        Posting postingReplay = new Posting();
        postingReplay.setDescription("Replay PostingId:" + postingReplayRequest.getPostingId());
        postingReplay.setUserId(postingReplayRequest.getUserId());
        postingReplay.setStatus(Posting.STATUS_PENDING);


        Optional<Posting> postingOptional = postingRepository.findById(postingReplayRequest.getPostingId());
        if (postingOptional.isPresent()) {
            List<PostingAssetMovement> postingAssetMovements = postingOptional.get().getPostingAssetMovements();
            for (PostingAssetMovement postingAssetMovement : postingAssetMovements) {

                PostingAssetMovement postingAssetMovementReplay = new PostingAssetMovement();
                postingAssetMovementReplay.setWalletAssetOwnershipId(postingAssetMovement.getWalletAssetOwnershipId());
                postingAssetMovementReplay.setWalletIdFrom(postingAssetMovement.getWalletIdFrom());
                postingAssetMovementReplay.setWalletIdTo(postingAssetMovement.getWalletIdTo());
                postingAssetMovementReplay.setPosting(postingReplay);
                postingAssetMovementRepository.save(postingAssetMovementReplay);
                postingReplay = postingRepository.save(postingReplay);
            }

        }

        if (postingReplay.getId() != null) {
            return PostingReplayResponse.builder()
                    .responseId(postingReplayRequest.getRequestId())
                    .postingId(postingReplay.getId())
                    .postingStatus(postingReplay.getStatus())
                    .build();
        } else {
            throw new GenericRestfulException("Server unable to create Posting");
        }
    }

    // return true to clear the posting, false to fail the positng
    @Override
    public boolean postingAssetsFromOneWalletToAnotherWallet(long postingId, long userId) throws GenericRestfulException {

        List<PostingAssetMovement> postingAssetMovements = postingAssetMovementRepository.findByPostingIdAndPosting_UserId(postingId, userId);
        if (postingAssetMovements.size() > 0) {
            // each Posting support multiple Asset Movements
            for (PostingAssetMovement postingAssetMovement : postingAssetMovements) {
                // For each Asset Movement
                Optional<WalletAssetOwnership> walletAssetOwnershipOptional = walletAssetOwnershipRepository.findByIdAndWalletIdAndWallet_Account_UserIdAndWallet_Account_AccountStatus(postingAssetMovement.getWalletAssetOwnershipId(), postingAssetMovement.getWalletIdFrom(), userId, Account.STATUS_OPEN);
                if (walletAssetOwnershipOptional.isPresent()) {
                    // the walletFrom belongs to Open Account of the user, also
                    // the asset belong to the walletFrom and
                    Optional<Wallet> walletToOptional = walletRepository.findByIdAndAccount_UserIdAndAccount_AccountStatus(postingAssetMovement.getWalletIdTo(), userId, Account.STATUS_OPEN);

                    if (walletToOptional.isPresent()) {
                        // the walletTo belongs to Open Account of the user

                        // this Asset Movement is viable NOW, make the movement
                        walletAssetOwnershipOptional.get().setWallet(walletToOptional.get());
                        walletAssetOwnershipRepository.save(walletAssetOwnershipOptional.get());
                    } else {
                        // transaction roll back
                        throw new GenericRestfulException("the walletTo do not belongs to Open Account of the user");
                    }

                } else {
                    // transaction roll back
                    throw new GenericRestfulException("the asset do not belongs to wallet or do not belongs to Open Account of the user");
                }

            }
        } else {
            return false;
        }

        return true;
    }

    @Override
    public AssetResponse getCurrentAssets(AssetRequest assetRequest) {
        Long userId = assetRequest.getUserId();

        List<Wallet> wallets = walletRepository.findByAccount_UserId(userId);

        List<WalletView> walletViews = wallets.stream().map(wallet -> {
            List<AssetView> assetViews = wallet.getWalletAssetOwnerships().stream().map(walletAssetOwnership ->
                    AssetView.builder()
                            .assetId(walletAssetOwnership.getAsset().getId())
                            .assetName(walletAssetOwnership.getAsset().getAssetName())
                            .units(walletAssetOwnership.getUnits())
                            .build()
            ).collect(Collectors.toList());

            return WalletView.builder()
                    .walletId(wallet.getId())
                    .walletName(wallet.getWalletName())
                    .assetViews(assetViews)
                    .build();
        }).collect(Collectors.toList());

        return AssetResponse.builder()
                .responseId(assetRequest.getRequestId())
                .userId(userId)
                .walletViews(walletViews)
                .build();

    }
}
