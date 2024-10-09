package com.silu.Service;

import com.silu.Models.Coin;
import com.silu.Models.User;
import com.silu.Models.WatchList;
import com.silu.Repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WatchListServiceImpl implements WatchListService {

    @Autowired
    private WatchlistRepository watchlistRepository;


    @Override
    public WatchList findUserWatchlist(Long userId) {
        WatchList watchList = watchlistRepository.findByUserId(userId);

        if (watchList == null) {
            throw new RuntimeException("Watchlist not found");
        }
        return watchList;
    }

    @Override
    public WatchList createWatchList(User user) {

        WatchList watchList = WatchList.builder()
                .user(user)
                .build();
        return watchlistRepository.save(watchList);
    }

    @Override
    public WatchList findById(Long id) {
        return watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Watchlist not found"));
    }

    @Override
    public Coin addItemToWatchList(Coin coin, User user) {
        WatchList watchList = findUserWatchlist(user.getId());
        if (watchList.getCoins().contains(coin)) {
            watchList.getCoins().remove(coin);
        } else watchList.getCoins().add(coin);

        watchlistRepository.save(watchList);
        return coin;
    }
}
