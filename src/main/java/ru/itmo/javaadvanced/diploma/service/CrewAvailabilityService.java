package ru.itmo.javaadvanced.diploma.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.javaadvanced.diploma.domain.entity.CrewMember;
import ru.itmo.javaadvanced.diploma.domain.enums.CrewMemberType;
import ru.itmo.javaadvanced.diploma.dto.crew.AvailableCrewResponse;
import ru.itmo.javaadvanced.diploma.dto.crew.CrewMemberShortView;
import ru.itmo.javaadvanced.diploma.repository.CrewMemberRepository;
import ru.itmo.javaadvanced.diploma.repository.FlightCrewRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class CrewAvailabilityService {

    private final CrewMemberRepository crewMemberRepository;
    private final FlightCrewRepository flightCrewRepository;

    public CrewAvailabilityService(CrewMemberRepository crewMemberRepository,
                                   FlightCrewRepository flightCrewRepository) {
        this.crewMemberRepository = crewMemberRepository;
        this.flightCrewRepository = flightCrewRepository;
    }

    public AvailableCrewResponse findAvailableCrew(OffsetDateTime intervalStart, OffsetDateTime intervalEnd) {
        Set<Long> busyCrewIds = flightCrewRepository.findBusyCrewMemberIds(intervalStart, intervalEnd);

        List<CrewMemberShortView> availablePilots = crewMemberRepository
                .findByCrewMemberType(CrewMemberType.PILOT)
                .stream()
                .filter(crewMember -> !busyCrewIds.contains(crewMember.getId()))
                .map(this::toShortView)
                .toList();

        List<CrewMemberShortView> availableAttendants = crewMemberRepository
                .findByCrewMemberType(CrewMemberType.FLIGHT_ATTENDANT)
                .stream()
                .filter(crewMember -> !busyCrewIds.contains(crewMember.getId()))
                .map(this::toShortView)
                .toList();

        return new AvailableCrewResponse(availablePilots, availableAttendants);
    }

    private CrewMemberShortView toShortView(CrewMember crewMember) {
        String fullName = crewMember.getLastName() + " " + crewMember.getFirstName()
                + (crewMember.getMiddleName() != null ? " " + crewMember.getMiddleName() : "");
        return new CrewMemberShortView(crewMember.getId(), fullName.trim(), crewMember.getPhoneNumber());
    }
}