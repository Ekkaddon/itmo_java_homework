package ru.itmo.javaadvanced.diploma.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.itmo.javaadvanced.diploma.domain.entity.Aircraft;
import ru.itmo.javaadvanced.diploma.domain.entity.Flight;
import ru.itmo.javaadvanced.diploma.dto.flight.FlightRequest;
import ru.itmo.javaadvanced.diploma.dto.flight.FlightResponse;
import ru.itmo.javaadvanced.diploma.exception.ResourceNotFoundException;
import ru.itmo.javaadvanced.diploma.repository.AircraftRepository;
import ru.itmo.javaadvanced.diploma.repository.CrewMemberRepository;
import ru.itmo.javaadvanced.diploma.repository.FlightRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FlightServiceTest {

    private FlightService flightService;
    private FlightRepository flightRepository;
    private AircraftRepository aircraftRepository;
    private CrewMemberRepository crewMemberRepository;
    private FlightMapper flightMapper;

    @BeforeEach
    void setUp() {
        flightRepository = mock(FlightRepository.class);
        aircraftRepository = mock(AircraftRepository.class);
        crewMemberRepository = mock(CrewMemberRepository.class);
        flightMapper = mock(FlightMapper.class);
        flightService = new FlightService(flightRepository, aircraftRepository, crewMemberRepository, flightMapper);
    }

    @Test
    @DisplayName("Получение всех рейсов")
    void shouldFindAllFlights() {
        // Given
        Flight flight1 = new Flight();
        flight1.setId(1L);
        flight1.setFlightNumber("SU1001");

        Flight flight2 = new Flight();
        flight2.setId(2L);
        flight2.setFlightNumber("SU2002");

        FlightResponse response1 = new FlightResponse(1L, "SU1001", "Москва", "СПб",
                OffsetDateTime.now(), OffsetDateTime.now().plusHours(2), 8000, null, List.of());
        FlightResponse response2 = new FlightResponse(2L, "SU2002", "Москва", "Калининград",
                OffsetDateTime.now(), OffsetDateTime.now().plusHours(3), 9000, null, List.of());

        when(flightRepository.findAll()).thenReturn(List.of(flight1, flight2));
        when(flightMapper.toResponse(flight1)).thenReturn(response1);
        when(flightMapper.toResponse(flight2)).thenReturn(response2);

        // When
        List<FlightResponse> result = flightService.findAllFlights();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("SU1001", result.get(0).flightNumber());
        assertEquals("SU2002", result.get(1).flightNumber());
    }

    @Test
    @DisplayName("Получение рейса по ID")
    void shouldFindFlightById() {
        // Given
        Long flightId = 1L;
        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setFlightNumber("SU1001");

        FlightResponse response = new FlightResponse(flightId, "SU1001", "Москва", "СПб",
                OffsetDateTime.now(), OffsetDateTime.now().plusHours(2), 8000, null, List.of());

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(flightMapper.toResponse(flight)).thenReturn(response);

        // When
        FlightResponse result = flightService.findFlightById(flightId);

        // Then
        assertNotNull(result);
        assertEquals("SU1001", result.flightNumber());
    }

    @Test
    @DisplayName("Поиск несуществующего рейса выбрасывает исключение")
    void shouldThrowExceptionWhenFlightNotFound() {
        // Given
        Long flightId = 999L;

        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> flightService.findFlightById(flightId));
    }

    @Test
    @DisplayName("Создание рейса")
    void shouldCreateFlight() {
        // Given
        OffsetDateTime departure = OffsetDateTime.now().plusDays(1);
        OffsetDateTime arrival = departure.plusHours(2);

        FlightRequest request = new FlightRequest("SU3003", "Москва", "Владивосток",
                departure, arrival, 1L, 12000, List.of());

        Aircraft aircraft = new Aircraft();
        aircraft.setId(1L);
        aircraft.setRegistrationNumber("RA-82045");

        Flight savedFlight = new Flight();
        savedFlight.setId(1L);
        savedFlight.setFlightNumber("SU3003");
        savedFlight.setAircraft(aircraft);

        FlightResponse response = new FlightResponse(1L, "SU3003", "Москва", "Владивосток",
                departure, arrival, 12000, null, List.of());

        when(aircraftRepository.findById(1L)).thenReturn(Optional.of(aircraft));
        when(crewMemberRepository.findAllById(any())).thenReturn(List.of());
        when(flightMapper.toEntity(any(), any(), any())).thenReturn(savedFlight);
        when(flightRepository.save(any(Flight.class))).thenReturn(savedFlight);
        when(flightMapper.toResponse(savedFlight)).thenReturn(response);

        // When
        FlightResponse result = flightService.createFlight(request);

        // Then
        assertNotNull(result);
        assertEquals("SU3003", result.flightNumber());
        verify(flightRepository).save(any(Flight.class));
    }

    @Test
    @DisplayName("Удаление рейса")
    void shouldDeleteFlight() {
        // Given
        Long flightId = 1L;
        Flight flight = new Flight();
        flight.setId(flightId);

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        // When
        flightService.deleteFlight(flightId);

        // Then
        verify(flightRepository).delete(flight);
    }

    @Test
    @DisplayName("Удаление несуществующего рейса выбрасывает исключение")
    void shouldThrowExceptionWhenDeletingNonExistentFlight() {
        // Given
        Long flightId = 999L;

        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> flightService.deleteFlight(flightId));
    }
}