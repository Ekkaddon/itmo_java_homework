package ru.itmo.javaadvanced.diploma.service;

import org.springframework.stereotype.Component;
import ru.itmo.javaadvanced.diploma.domain.entity.Aircraft;
import ru.itmo.javaadvanced.diploma.domain.entity.CrewMember;
import ru.itmo.javaadvanced.diploma.domain.entity.Flight;
import ru.itmo.javaadvanced.diploma.domain.entity.FlightCrew;
import ru.itmo.javaadvanced.diploma.dto.flight.*;
import ru.itmo.javaadvanced.diploma.exception.ResourceNotFoundException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class FlightMapper {

    public Flight toEntity(FlightRequest request, Aircraft aircraft, Map<Long, CrewMember> crewMemberById) {
        Flight flight = Flight.builder()
                .flightNumber(request.flightNumber())
                .originCity(request.originCity())
                .destinationCity(request.destinationCity())
                .scheduledDeparture(request.scheduledDeparture())
                .scheduledArrival(request.scheduledArrival())
                .plannedPayloadKg(request.plannedPayloadKg())
                .aircraft(aircraft)
                .build();

        if (request.crew() != null && !request.crew().isEmpty()) {
            Set<FlightCrew> crewAssignments = new LinkedHashSet<>();
            for (FlightCrewMemberRequest crewRequest : request.crew()) {
                CrewMember crewMember = getCrewMemberOrThrow(crewMemberById, crewRequest.crewMemberId());
                FlightCrew flightCrew = FlightCrew.builder()
                        .crewMember(crewMember)
                        .crewRole(crewRequest.crewRole())
                        .flight(flight)
                        .build();
                crewAssignments.add(flightCrew);
            }
            flight.setCrewAssignments(crewAssignments);
        }

        return flight;
    }

    public void updateEntity(Flight flight, FlightRequest request, Aircraft aircraft, Map<Long, CrewMember> crewMemberById) {
        flight.setFlightNumber(request.flightNumber());
        flight.setOriginCity(request.originCity());
        flight.setDestinationCity(request.destinationCity());
        flight.setScheduledDeparture(request.scheduledDeparture());
        flight.setScheduledArrival(request.scheduledArrival());
        flight.setPlannedPayloadKg(request.plannedPayloadKg());
        flight.setAircraft(aircraft);

        Set<FlightCrew> crewAssignments = new LinkedHashSet<>();
        if (request.crew() != null) {
            for (FlightCrewMemberRequest crewRequest : request.crew()) {
                CrewMember crewMember = getCrewMemberOrThrow(crewMemberById, crewRequest.crewMemberId());
                FlightCrew flightCrew = FlightCrew.builder()
                        .crewMember(crewMember)
                        .crewRole(crewRequest.crewRole())
                        .flight(flight)
                        .build();
                crewAssignments.add(flightCrew);
            }
        }
        flight.getCrewAssignments().clear();
        flight.getCrewAssignments().addAll(crewAssignments);
    }

    public FlightResponse toResponse(Flight flight) {
        Aircraft aircraft = flight.getAircraft();
        AircraftSummary aircraftSummary = new AircraftSummary(
                aircraft.getId(),
                aircraft.getRegistrationNumber(),
                aircraft.getAircraftModel()
        );

        List<FlightCrewMemberResponse> crewMembers = flight.getCrewAssignments()
                .stream()
                .map(flightCrew -> {
                    CrewMember crewMember = flightCrew.getCrewMember();
                    String fullName = crewMember.getLastName() + " " + crewMember.getFirstName()
                            + (crewMember.getMiddleName() != null ? " " + crewMember.getMiddleName() : "");
                    return new FlightCrewMemberResponse(
                            crewMember.getId(),
                            fullName.trim(),
                            flightCrew.getCrewRole(),
                            crewMember.getPhoneNumber()
                    );
                })
                .toList();

        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getOriginCity(),
                flight.getDestinationCity(),
                flight.getScheduledDeparture(),
                flight.getScheduledArrival(),
                flight.getPlannedPayloadKg(),
                aircraftSummary,
                crewMembers
        );
    }

    private CrewMember getCrewMemberOrThrow(Map<Long, CrewMember> crewMemberById, Long crewMemberId) {
        CrewMember crewMember = crewMemberById.get(crewMemberId);
        if (crewMember == null) {
            throw new ResourceNotFoundException("Член экипажа с идентификатором %d не найден".formatted(crewMemberId));
        }
        return crewMember;
    }
}