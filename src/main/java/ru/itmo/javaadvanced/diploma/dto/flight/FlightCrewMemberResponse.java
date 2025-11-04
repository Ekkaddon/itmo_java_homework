package ru.itmo.javaadvanced.diploma.dto.flight;

public record FlightCrewMemberResponse(
        Long crewMemberId,
        String fullName,
        String crewRole,
        String phoneNumber
) {
}