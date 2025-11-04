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
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getAllCountries_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void createCountry_ValidData_ReturnsCreated() throws Exception {
        String countryJson = """
            {
                "code": "US",
                "nameRu": "США",
                "nameEn": "United States"
            }
            """;

        mockMvc.perform(post("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(countryJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("US"));
    }

    @Test
    @WithMockUser
    void createCountry_InvalidData_ReturnsBadRequest() throws Exception {
        String invalidCountryJson = """
            {
                "code": "A",
                "nameRu": "",
                "nameEn": "Test"
            }
            """;

        mockMvc.perform(post("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCountryJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCountries_Unauthorized_Returns401() throws Exception {
        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isUnauthorized());
    }
}

