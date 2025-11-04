package ru.itmo.javaadvanced.diploma;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DiplomaApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String dispatcherToken;
    private String pilotToken;

    @BeforeEach
    void setUp() throws Exception {
        dispatcherToken = obtainToken("dispatcher", "password");
        pilotToken = obtainToken("pilot", "password");
    }

    @Test
    @DisplayName("Авторизация возвращает JWT токен")
    void shouldAuthenticateUser() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", "attendant",
                                "password", "password"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    @DisplayName("Получение профиля текущего пользователя")
    void shouldReturnProfile() throws Exception {
        mockMvc.perform(get("/api/profile")
                        .header("Authorization", bearer(pilotToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Иванов Алексей Петрович"))
                .andExpect(jsonPath("$.crewMemberType").value("PILOT"));
    }

    @Test
    @DisplayName("CRUD операций с новостями")
    void shouldManageNewsLifecycle() throws Exception {
        String createPayload = objectMapper.writeValueAsString(new NewsPayload("Новая новость", "Содержимое"));

        MvcResult createResult = mockMvc.perform(post("/api/news")
                        .header("Authorization", bearer(dispatcherToken))
                        .contentType(APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Новая новость"))
                .andReturn();

        long newsId = extractId(createResult);

        mockMvc.perform(get("/api/news")
                        .header("Authorization", bearer(dispatcherToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItem((int) newsId)));

        String updatePayload = objectMapper.writeValueAsString(new NewsPayload("Обновленная новость", "Новое содержание"));

        mockMvc.perform(put("/api/news/{id}", newsId)
                        .header("Authorization", bearer(dispatcherToken))
                        .contentType(APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Обновленная новость"));

        mockMvc.perform(delete("/api/news/{id}", newsId)
                        .header("Authorization", bearer(dispatcherToken)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("CRUD операций с рейсами")
    void shouldManageFlightLifecycle() throws Exception {
        OffsetDateTime departure = OffsetDateTime.now().plusDays(5);
        OffsetDateTime arrival = departure.plusHours(2);
        String flightNumber = "FLT-" + UUID.randomUUID();

        FlightPayload createPayload = new FlightPayload(
                flightNumber,
                "Москва",
                "Новосибирск",
                departure,
                arrival,
                1L,
                9000,
                List.of(
                        new FlightCrewPayload(1L, "CAPTAIN"),
                        new FlightCrewPayload(2L, "FLIGHT_ATTENDANT")
                ));

        MvcResult createResult = mockMvc.perform(post("/api/flights")
                        .header("Authorization", bearer(dispatcherToken))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPayload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flightNumber").value(flightNumber))
                .andReturn();

        long flightId = extractId(createResult);

        mockMvc.perform(get("/api/flights/{id}", flightId)
                        .header("Authorization", bearer(dispatcherToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value(flightNumber));

        FlightPayload updatePayload = new FlightPayload(
                flightNumber,
                "Москва",
                "Екатеринбург",
                departure.plusDays(1),
                arrival.plusDays(1),
                1L,
                8500,
                List.of(
                        new FlightCrewPayload(1L, "CAPTAIN"),
                        new FlightCrewPayload(2L, "FLIGHT_ATTENDANT")
                ));

        mockMvc.perform(put("/api/flights/{id}", flightId)
                        .header("Authorization", bearer(dispatcherToken))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destinationCity").value("Екатеринбург"));

        mockMvc.perform(delete("/api/flights/{id}", flightId)
                        .header("Authorization", bearer(dispatcherToken)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Получение доступного экипажа")
    void shouldReturnAvailableCrew() throws Exception {
        OffsetDateTime start = OffsetDateTime.now().plusDays(10);
        OffsetDateTime end = start.plusHours(4);

        mockMvc.perform(get("/api/flights/available-crew")
                        .header("Authorization", bearer(dispatcherToken))
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pilots", not(empty())))
                .andExpect(jsonPath("$.flightAttendants", not(empty())));
    }

    @Test
    @DisplayName("Доступность OpenAPI документации")
    void shouldExposeOpenApiDocs() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    private String obtainToken(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", username,
                                "password", password
                        ))))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        return jsonNode.get("accessToken").asText();
    }

    private long extractId(MvcResult mvcResult) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        return jsonNode.get("id").asLong();
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }

    private record NewsPayload(String title, String content) {
    }

    private record FlightPayload(
            String flightNumber,
            String originCity,
            String destinationCity,
            OffsetDateTime scheduledDeparture,
            OffsetDateTime scheduledArrival,
            Long aircraftId,
            Integer plannedPayloadKg,
            List<FlightCrewPayload> crew
    ) {
    }

    private record FlightCrewPayload(Long crewMemberId, String crewRole) {
    }
}