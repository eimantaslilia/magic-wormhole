package com.magic.wormhole;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RegistrarClient {

    private static final String URI = "localhost:8080";

    public ResponseEntity<ClientAddress> fetchClientAddress(String clientName) {
        return WebClient.create()
                .get()
                .uri(URI + "/fetch/" + clientName)
                .retrieve()
                .toEntity(ClientAddress.class)
                .block();
    }

    public void register(RegistrationRequest request) {
        WebClient.create()
                .post()
                .uri(URI + "/register")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(err -> Mono.error(new RuntimeException("Failed to register due to: " + err))))
                .bodyToMono(Void.class)
                .block();
    }
}
