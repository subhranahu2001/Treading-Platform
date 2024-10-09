package com.silu.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silu.Models.Coin;
import com.silu.Service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Coin>> getCoinList(@RequestParam(required = false,name = "page") int page) {
        List<Coin> coinList = coinService.getCoinList(page);

        return ResponseEntity.ok(coinList);
    }

    @GetMapping("/{coinId}/chart")
    public ResponseEntity<JsonNode> getMarketChart(@RequestParam("days") int days,
                                                    @PathVariable String coinId) throws Exception {
        String res = coinService.getMarketChat(coinId,days);
        JsonNode node = objectMapper.readTree(res);
        return ResponseEntity.ok(node);
    }

    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchCoin(@RequestParam("keyword") String keyword ) throws Exception {
        String coin = coinService.searchCoin(keyword);
        JsonNode node = objectMapper.readTree(coin);
        return ResponseEntity.ok(node);
    }

    @GetMapping("/top50")
    public ResponseEntity<JsonNode> getTop50CoinByMarketCapRank() throws Exception {
        String coin = coinService.getTop50CoinsByMarketCapRank();
        JsonNode node = objectMapper.readTree(coin);
        return ResponseEntity.ok(node);
    }

    @GetMapping("/trending")
    public ResponseEntity<JsonNode> getTrendingCoin() throws Exception {
        String coin = coinService.getTreadingCoins();
        JsonNode node = objectMapper.readTree(coin);
        return ResponseEntity.ok(node);
    }

    @GetMapping("/details/{coinId}")
    public ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId) throws Exception {
        String coin = coinService.getCoinDetails(coinId);
        JsonNode node = objectMapper.readTree(coin);
        return ResponseEntity.ok(node);
    }




}
