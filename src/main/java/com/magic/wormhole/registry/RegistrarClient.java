package com.magic.wormhole.registry;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class RegistrarClient {

    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "8080";

    public static ClientAddress fetchClientAddress(String registryIP, String clientName) {
        return WebClient.create()
                .get()
                .uri(getUri(registryIP) + "/fetch/" + clientName)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> Mono.error(new RuntimeException("Server returned an error when fetching client by name: " + clientName)))
                .toEntity(ClientAddress.class)
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Server returned no entity when fetching by: " + clientName))
                .getBody();
    }

    public static void register(String registryIP, RegistrationRequest request) {
        WebClient.create()
                .post()
                .uri(getUri(registryIP) + "/register")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(err -> Mono.error(new RuntimeException("Failed to register due to: " + err))))
                .bodyToMono(Void.class)
                .block();
    }

    private static String getUri(String registryIP) {
        String hostname = Optional.ofNullable(registryIP)
                .map(host -> "http://" + host)
                .orElse(DEFAULT_HOST);
        return hostname + ":" + DEFAULT_PORT;
    }
}
