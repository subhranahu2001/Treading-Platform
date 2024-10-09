package com.silu.Controller;

import com.silu.Models.Asset;
import com.silu.Models.User;
import com.silu.Service.AssetService;
import com.silu.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long assetId
    ) {
        Asset asset = assetService.getAssetById(assetId);
        return ResponseEntity.ok(asset);
    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId
    ) {
        User user = userService.findUserProfileByJwt(jwt);
        Asset asset = assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok(asset);
    }

    @GetMapping()
    public ResponseEntity<List<Asset>> getAssetForUser(
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserProfileByJwt(jwt);
        List<Asset> asset = assetService.getUsersAssets(user.getId());
        return ResponseEntity.ok(asset);
    }

}
