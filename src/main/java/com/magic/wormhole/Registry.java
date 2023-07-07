package com.magic.wormhole;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Registry {

    private final Map<String, ClientAddress> addressMap = new HashMap<>();

    public void register(RegistrationRequest req, String hostname) {
        addressMap.put(req.name(), new ClientAddress(hostname, req.port()));
        //name, hostname, port
    }

    public ClientAddress getAddress(String clientName) {
        return addressMap.get(clientName);
    }
 }