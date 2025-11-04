package ru.itmo.javaadvanced.homework6.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getAllRegions_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/regions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void createRegion_ValidData_ReturnsCreated() throws Exception {
        String regionJson = """
            {
                "code": "50",
                "nameRu": "Московская область",
                "nameEn": "Moscow Region"
            }
            """;

        mockMvc.perform(post("/api/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(regionJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("50"));
    }

    @Test
    @WithMockUser
    void createRegion_InvalidData_ReturnsBadRequest() throws Exception {
        String invalidRegionJson = """
            {
                "code": "A",
                "nameRu": "",
                "nameEn": "Test"
            }
            """;

        mockMvc.perform(post("/api/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRegionJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllRegions_Unauthorized_Returns401() throws Exception {
        mockMvc.perform(get("/api/regions"))
                .andExpect(status().isUnauthorized());
    }
}

