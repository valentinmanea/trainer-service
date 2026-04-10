package com.trainerservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BlandAIRequestInterceptor implements RequestInterceptor {

    @Value("${bland.ai.api.key}")
    private String apiKey;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", apiKey);
    }
}
