package ru.itmo.javaadvanced.diploma.dto.flight;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FlightRequest(
        @NotBlank
        String flightNumber,
        @NotBlank
        String originCity,
        @NotBlank
        String destinationCity,
        @NotNull
        @Future
        OffsetDateTime scheduledDeparture,
        @NotNull
        @Future
        OffsetDateTime scheduledArrival,
        @NotNull
        @Positive
        Long aircraftId,
        Integer plannedPayloadKg,
        List<FlightCrewMemberRequest> crew
) {
}