package ru.itmo.javaadvanced.diploma.dto.flight;

import java.time.OffsetDateTime;
import java.util.List;

public record FlightResponse(
        Long id,
        String flightNumber,
        String originCity,
        String destinationCity,
        OffsetDateTime scheduledDeparture,
        OffsetDateTime scheduledArrival,
        Integer plannedPayloadKg,
        AircraftSummary aircraft,
        List<FlightCrewMemberResponse> crew
) {
}