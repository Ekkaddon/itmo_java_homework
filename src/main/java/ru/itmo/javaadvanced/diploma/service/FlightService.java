package ru.itmo.javaadvanced.diploma.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.javaadvanced.diploma.domain.entity.Aircraft;
import ru.itmo.javaadvanced.diploma.domain.entity.CrewMember;
import ru.itmo.javaadvanced.diploma.domain.entity.Flight;
import ru.itmo.javaadvanced.diploma.dto.flight.FlightCrewMemberRequest;
import ru.itmo.javaadvanced.diploma.dto.flight.FlightRequest;
import ru.itmo.javaadvanced.diploma.dto.flight.FlightResponse;
import ru.itmo.javaadvanced.diploma.exception.ResourceNotFoundException;
import ru.itmo.javaadvanced.diploma.repository.AircraftRepository;
import ru.itmo.javaadvanced.diploma.repository.CrewMemberRepository;
import ru.itmo.javaadvanced.diploma.repository.FlightRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FlightService {

    private final FlightRepository flightRepository;
    private final AircraftRepository aircraftRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final FlightMapper flightMapper;

    public FlightService(FlightRepository flightRepository,
                         AircraftRepository aircraftRepository,
                         CrewMemberRepository crewMemberRepository,
                         FlightMapper flightMapper) {
        this.flightRepository = flightRepository;
        this.aircraftRepository = aircraftRepository;
        this.crewMemberRepository = crewMemberRepository;
        this.flightMapper = flightMapper;
    }

    public List<FlightResponse> findAllFlights() {
        return flightRepository.findAll()
                .stream()
                .map(flightMapper::toResponse)
                .toList();
    }

    public FlightResponse findFlightById(Long id) {
        return flightRepository.findById(id)
                .map(flightMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Рейс не найден"));
    }

    @Transactional
    public FlightResponse createFlight(FlightRequest request) {
        Aircraft aircraft = aircraftRepository.findById(request.aircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Воздушное судно не найдено"));

        List<CrewMember> crewMembers = crewMemberRepository.findAllById(
                request.crew() != null ? request.crew().stream().map(FlightCrewMemberRequest::crewMemberId).toList() : List.of()
        );

        Flight flight = flightMapper.toEntity(request, aircraft, crewMembers.stream()
                .collect(java.util.stream.Collectors.toMap(CrewMember::getId, crewMember -> crewMember)));
        return flightMapper.toResponse(flightRepository.save(flight));
    }

    @Transactional
    public FlightResponse updateFlight(Long id, FlightRequest request) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Рейс не найден"));

        Aircraft aircraft = aircraftRepository.findById(request.aircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Воздушное судно не найдено"));

        List<CrewMember> crewMembers = crewMemberRepository.findAllById(
                request.crew() != null ? request.crew().stream().map(FlightCrewMemberRequest::crewMemberId).toList() : List.of()
        );

        flightMapper.updateEntity(flight, request, aircraft, crewMembers.stream()
                .collect(java.util.stream.Collectors.toMap(CrewMember::getId, crewMember -> crewMember)));
        return flightMapper.toResponse(flight);
    }

    @Transactional
    public void deleteFlight(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Рейс не найден"));
        flightRepository.delete(flight);
    }

    public List<FlightResponse> findFlightsBetween(OffsetDateTime start, OffsetDateTime end) {
        return flightRepository.findByScheduledDepartureBetween(start, end)
                .stream()
                .map(flightMapper::toResponse)
                .toList();
    }
}
