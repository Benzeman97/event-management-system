package com.event.api.util;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RestTemplateUtil  {

    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }
}
