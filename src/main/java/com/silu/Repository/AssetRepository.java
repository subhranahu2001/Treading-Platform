package com.silu.Repository;

import com.silu.Models.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset,Long> {

    List<Asset> findByUserId(long userId);

    Asset findByUserIdAndCoinId(long userId, String coinId);

}
