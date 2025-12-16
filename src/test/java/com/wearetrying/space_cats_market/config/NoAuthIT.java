package com.wearetrying.space_cats_market.config;

import com.wearetrying.space_cats_market.AbstractIt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("no-auth")
@AutoConfigureMockMvc
public class NoAuthIT extends AbstractIt {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAllowAccess_WithoutToken_WhenNoAuthProfileIsActive() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }
}