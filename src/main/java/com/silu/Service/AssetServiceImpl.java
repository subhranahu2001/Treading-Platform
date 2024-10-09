package com.silu.Service;

import com.silu.Models.Asset;
import com.silu.Models.Coin;
import com.silu.Models.User;
import com.silu.Repository.AssetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;


    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {

        Asset asset = Asset.builder()
                .user(user)
                .quantity(quantity)
                .coin(coin)
                .buyPrice(coin.getCurrentPrice())
                .build();

        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) {
        return assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
        return null;
    }

    @Override
    public List<Asset> getUsersAssets(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) {
        Asset oldAsset = getAssetById(assetId);
        oldAsset.setQuantity(quantity);

        return assetRepository.save(oldAsset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        return assetRepository.findByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {

        assetRepository.deleteById(assetId);
    }
}
