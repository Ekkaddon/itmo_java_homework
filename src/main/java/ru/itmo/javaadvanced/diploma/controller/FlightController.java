package ru.itmo.javaadvanced.diploma.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.javaadvanced.diploma.dto.crew.AvailableCrewResponse;
import ru.itmo.javaadvanced.diploma.dto.flight.FlightRequest;
import ru.itmo.javaadvanced.diploma.dto.flight.FlightResponse;
import ru.itmo.javaadvanced.diploma.service.CrewAvailabilityService;
import ru.itmo.javaadvanced.diploma.service.FlightService;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;
    private final CrewAvailabilityService crewAvailabilityService;

    public FlightController(FlightService flightService, CrewAvailabilityService crewAvailabilityService) {
        this.flightService = flightService;
        this.crewAvailabilityService = crewAvailabilityService;
    }

    @Operation(summary = "Получение списка рейсов")
    @GetMapping
    public ResponseEntity<List<FlightResponse>> findAllFlights() {
        return ResponseEntity.ok(flightService.findAllFlights());
    }

    @Operation(summary = "Получение информации о рейсе")
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> findFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.findFlightById(id));
    }

    @Operation(summary = "Добавление рейса")
    @PostMapping
    @PreAuthorize("hasRole('DISPATCHER')")
    public ResponseEntity<FlightResponse> createFlight(@Validated @RequestBody FlightRequest flightRequest) {
        FlightResponse createdFlight = flightService.createFlight(flightRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFlight);
    }

    @Operation(summary = "Редактирование рейса")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public ResponseEntity<FlightResponse> updateFlight(@PathVariable Long id,
                                                       @Validated @RequestBody FlightRequest flightRequest) {
        return ResponseEntity.ok(flightService.updateFlight(id, flightRequest));
    }

    @Operation(summary = "Удаление рейса")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получение доступного экипажа")
    @GetMapping("/available-crew")
    public ResponseEntity<AvailableCrewResponse> findAvailableCrew(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime intervalStart,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime intervalEnd) {
        validateInterval(intervalStart, intervalEnd);
        return ResponseEntity.ok(crewAvailabilityService.findAvailableCrew(intervalStart, intervalEnd));
    }

    private void validateInterval(OffsetDateTime intervalStart, OffsetDateTime intervalEnd) {
        if (!intervalEnd.isAfter(intervalStart)) {
            throw new IllegalArgumentException("Время окончания должно быть позже начала интервала");
        }
    }
}