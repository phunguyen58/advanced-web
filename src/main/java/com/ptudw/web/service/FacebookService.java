package com.ptudw.web.service;

import com.ptudw.web.domain.FacebookValidationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacebookService {

    @Value("${facebook.app-id}")
    private String facebookAppId;

    @Value("${facebook.app-secret}")
    private String facebookAppSecret;

    public FacebookValidationResult validateAccessToken(String accessToken) {
        String url = String.format(
            "https://graph.facebook.com/debug_token?input_token=%s&access_token=%s|%s",
            accessToken,
            facebookAppId,
            facebookAppSecret
        );

        RestTemplate restTemplate = new RestTemplate();
        FacebookValidationResult validationResult = restTemplate.getForObject(url, FacebookValidationResult.class);
        return validationResult;
    }
}
