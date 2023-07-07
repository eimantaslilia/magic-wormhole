package com.magic.wormhole.registry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class Registrar {

    @Autowired
    private Registry registry;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void register(HttpServletRequest request, @RequestBody @Valid RegistrationRequest registrationRequest) {
        String hostname = request.getRemoteAddr();
        System.out.println("Registering: " + registrationRequest + " with hostname: " + hostname);
        registry.register(registrationRequest, hostname);
    }


    @GetMapping("/fetch/{clientName}")
    public ResponseEntity<ClientAddress> getClientAddress(@PathVariable String clientName) {
        var address = registry.getAddress(clientName);
        System.out.println("Fetching client by name: [" + clientName + "], found: " + address);
        return ResponseEntity.of(Optional.ofNullable(address));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleArgumentNotValidExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
