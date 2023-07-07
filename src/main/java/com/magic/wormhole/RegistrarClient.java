package com.magic.wormhole;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RegistrarClient {

    public ResponseEntity<ClientAddress> fetchClientAddress(String clientName) {
        return WebClient.create()
                .get()
                .uri("localhost:8080/fetch/" + clientName)
                .retrieve()
                .toEntity(ClientAddress.class)
                .block();
    }
}
