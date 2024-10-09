package com.silu.Repository;

import com.silu.Models.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchList,Long> {

    WatchList findByUserId(Long userId);
}
