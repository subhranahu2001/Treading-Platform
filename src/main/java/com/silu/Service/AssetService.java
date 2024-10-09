package com.silu.Service;

import com.silu.Models.Asset;
import com.silu.Models.Coin;
import com.silu.Models.User;

import java.util.List;

public interface AssetService {

    Asset createAsset(User user, Coin coin,double quantity);

    Asset getAssetById(Long assetId);

    Asset getAssetByUserIdAndId(Long userId, Long assetId);

    List<Asset> getUsersAssets(Long userId);

    Asset updateAsset(Long assetId,double quantity);

    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);

    void deleteAsset(Long assetId);
}
