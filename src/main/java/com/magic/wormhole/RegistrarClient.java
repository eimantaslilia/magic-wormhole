package com.magic.wormhole;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class RegistrarClient {

    private static final String URI = "localhost:8080";

    public static ClientAddress fetchClientAddress(String clientName) {
        return WebClient.create()
                .get()
                .uri(URI + "/fetch/" + clientName)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> Mono.error(new RuntimeException("Server returned an error when fetching client by name: " + clientName)))
                .toEntity(ClientAddress.class)
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Server returned no entity when fetching by: " + clientName))
                .getBody();
    }

    public static void register(RegistrationRequest request) {
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
