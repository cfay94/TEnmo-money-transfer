package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AccountInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL;


    public AccountService(String base_url) {
        BASE_URL = base_url;
    }

    public BigDecimal getBalance(String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity entity = new HttpEntity(httpHeaders);
        BigDecimal response = restTemplate.exchange("http://localhost:8080/balance",
                HttpMethod.GET, entity, BigDecimal.class).getBody();
        return response;
    }
}
