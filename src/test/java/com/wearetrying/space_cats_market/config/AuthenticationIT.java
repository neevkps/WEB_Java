package com.wearetrying.space_cats_market.config;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAllowAccess_WhenApiKeyIsCorrect() throws Exception {
        mockMvc.perform(get("/products")
                        .header("X-COSMO-KEY", "meow-secret-key-123"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDenyAccess_WhenApiKeyIsMissing() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{\"error\": \"Access Denied: Invalid or missing Cosmo-Key. Only cats allowed!\"}"));
    }

    @Test
    void shouldDenyAccess_WhenApiKeyIsInvalid() throws Exception {
        mockMvc.perform(get("/products")
                        .header("X-COSMO-KEY", "wrong-key"))
                .andExpect(status().isUnauthorized());
    }
}

