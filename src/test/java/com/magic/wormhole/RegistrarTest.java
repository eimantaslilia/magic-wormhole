package com.magic.wormhole;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RegistrarTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Registry registry;

    @Test
    void shouldRegister() throws Exception {
        var registration = new RegistrationRequest("receiver", 9010);
        mockMvc.perform(post("/register")
                        .content(objectMapper.writeValueAsString(registration))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        var result = registry.getAddress("receiver");

        assertEquals(result.port(), 9010);
    }

    @Test
    void shouldFetchClientInfo() throws Exception {
        registry.register(new RegistrationRequest("recv", 1020), "localhost");

        mockMvc.perform(get("/fetch/recv"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotFindClientInfo() throws Exception {
        mockMvc.perform(get("/fetch/recv"))
                .andExpect(status().isNotFound());
    }
}