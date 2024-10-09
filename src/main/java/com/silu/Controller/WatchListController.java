package com.silu.Controller;


import com.silu.Models.Coin;
import com.silu.Models.User;
import com.silu.Models.WatchList;
import com.silu.Service.CoinService;
import com.silu.Service.UserService;
import com.silu.Service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private UserService  userService;

    @Autowired
    private CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchlist(
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserProfileByJwt(jwt);
        WatchList watchList = watchListService.findUserWatchlist(user.getId());
        return ResponseEntity.ok(watchList);
    }

//    @PostMapping("/create")
//    public ResponseEntity<WatchList> createWatchlist(
//            @RequestHeader("Authorization") String jwt
//    ) {
//        User user = userService.findUserProfileByJwt(jwt);
//        WatchList watchList = watchListService.findUserWatchlist(user.getId());
//        return ResponseEntity.ok(watchList);
//    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchList(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId
    ) {
        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.finById(coinId);
        Coin addCoin = watchListService.addItemToWatchList(coin, user);
        return ResponseEntity.ok(addCoin);
    }



}
