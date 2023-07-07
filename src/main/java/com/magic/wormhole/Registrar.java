package com.magic.wormhole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class Registrar {

    @Autowired
    private Registry registry;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(HttpServletRequest request, @RequestBody RegistrationRequest registrationRequest) {
        String hostname = request.getRemoteAddr();
        registry.register(registrationRequest, hostname);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/fetch/{clientName}")
    public ResponseEntity<ClientAddress> getClientAddress(@PathVariable String clientName) {
        var address = registry.getAddress(clientName);
        return ResponseEntity.of(Optional.ofNullable(address));
    }
}
