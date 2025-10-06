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
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getAllCities_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/cities"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void createCity_ValidData_ReturnsCreated() throws Exception {
        String cityJson = """
            {
                "code": "TEST",
                "nameRu": "Тест",
                "nameEn": "Test",
                "population": 1000000
            }
            """;

        mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cityJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("TEST"));
    }

    @Test
    @WithMockUser
    void createCity_InvalidData_ReturnsBadRequest() throws Exception {
        String invalidCityJson = """
            {
                "code": "A",
                "nameRu": "",
                "nameEn": "Test",
                "population": -1000
            }
            """;

        mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCityJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCities_Unauthorized_Returns401() throws Exception {
        mockMvc.perform(get("/api/cities"))
                .andExpect(status().isUnauthorized());
    }
}

