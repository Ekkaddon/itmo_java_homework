package ru.itmo.javaadvanced.diploma.dto.flight;

public record AircraftSummary(
        Long id,
        String registrationNumber,
        String aircraftModel
) {
}