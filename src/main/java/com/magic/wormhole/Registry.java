package com.magic.wormhole;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Registry {

    private final Map<String, ClientAddress> addressMap = new HashMap<>();

    public void register(RegistrationRequest req, String hostname) {
        var address = new ClientAddress(hostname, req.port());
        addressMap.put(req.name(), address);

        System.out.println("New registry entry, name: " + req.name() + " address: " + address);
    }

    public ClientAddress getAddress(String clientName) {
        return addressMap.get(clientName);
    }
 }