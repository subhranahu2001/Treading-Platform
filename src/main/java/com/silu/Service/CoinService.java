package com.silu.Service;

import com.silu.Models.Coin;

import java.util.List;

public interface CoinService {

    List<Coin> getCoinList(int page);

    String getMarketChat(String coinId,int days) throws Exception;

    String getCoinDetails(String coinId) throws Exception;

    Coin finById(String coinId);

    String searchCoin(String keyWord) throws Exception;

    String getTop50CoinsByMarketCapRank() throws Exception;

    String getTreadingCoins() throws Exception;
}
