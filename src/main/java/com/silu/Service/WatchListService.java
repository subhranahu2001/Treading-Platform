package com.silu.Service;

import com.silu.Models.Coin;
import com.silu.Models.User;
import com.silu.Models.WatchList;

public interface WatchListService {

    WatchList findUserWatchlist(Long userId);

    WatchList createWatchList(User user);

    WatchList findById(Long id);

    Coin addItemToWatchList(Coin coin, User user);

}
