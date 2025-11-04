package ru.itmo.javaadvanced.diploma.dto.crew;

import java.util.List;

public record AvailableCrewResponse(
        List<CrewMemberShortView> pilots,
        List<CrewMemberShortView> flightAttendants
) {
}