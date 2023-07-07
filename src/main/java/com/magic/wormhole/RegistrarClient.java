package com.magic.wormhole;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component
public class RegistrarClient {

    public Optional<ClientAddress> fetchClientAddress(String clientName) {
        return WebClient.create()
                .get()
                .uri("localhost:8080/fetch/" + clientName)
                .retrieve()
                .bodyToMono(ClientAddress.class)
                .blockOptional();
    }
}
