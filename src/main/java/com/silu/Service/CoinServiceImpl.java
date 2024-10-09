package com.silu.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silu.Models.Coin;
import com.silu.Repository.CoinRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
//    private RestTemplate;

    @Override
    public List<Coin> getCoinList(int page) {

        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;


        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity =
                    new HttpEntity<>("parameters",headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            List<Coin> coinList = objectMapper.readValue(response.getBody(), new TypeReference<>(){});
            return coinList;
        } catch (HttpClientErrorException | HttpServerErrorException | JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return List.of();
    }

    @Override
    public String getMarketChat(String coinId, int days) throws Exception {

        String url = "https://api.coingecko.com/api/v3/coins/"+coinId+"/market_chart?vs_currency=usd&days="+days ;


        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity =
                    new HttpEntity<>("parameters",headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getCoinDetails(String coinId) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/"+coinId;


        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity =
                    new HttpEntity<>("parameters",headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode marketData = jsonNode.get("market_data");
            log.info(marketData.toString());
            Coin coin = Coin.builder()
                    .id(jsonNode.get("id").asText())
                    .name(jsonNode.get("name").asText())
                    .symbol(jsonNode.get("symbol").asText())
                    .image(jsonNode.get("image").get("large").asText())
                    //this for market data
                    .currentPrice(marketData.get("current_price").get("usd").asDouble())
                    .marketCap(marketData.get("market_cap").get("usd").asLong())
                    .marketCapRank(marketData.get("market_cap_rank").asInt())
                    .totalVolume(marketData.get("total_volume").get("usd").asLong())
                    .high24h(marketData.get("high_24h").get("usd").asDouble())
                    .low24h(marketData.get("low_24h").get("usd").asDouble())
                    .priceChange24h(marketData.get("price_change_24h").asDouble())
                    .priceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble())
                    .marketCapChange24h(marketData.get("market_cap_change_24h").asLong())
                    .marketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asLong())
                    .totalSupply(marketData.get("total_supply").asDouble())

                    .build();
            coinRepository.save(coin);
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Coin finById(String coinId) {

        return coinRepository.findById(coinId)
                .orElseThrow(() -> new RuntimeException("Coin Not Found"));
    }

    @Override
    public String searchCoin(String keyWord) throws Exception {

        String url = "https://api.coingecko.com/api/v3/search?query="+keyWord;


        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity =
                    new HttpEntity<>("parameters",headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public String getTop50CoinsByMarketCapRank() throws Exception {

        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=50&page=1";


        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity =
                    new HttpEntity<>("parameters",headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public String getTreadingCoins() throws Exception {

        String url = "https://api.coingecko.com/api/v3/search/trending";


        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity =
                    new HttpEntity<>("parameters",headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }

    }
}
