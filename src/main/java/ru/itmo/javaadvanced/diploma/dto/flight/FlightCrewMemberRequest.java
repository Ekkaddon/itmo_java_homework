package ru.itmo.javaadvanced.diploma.dto.flight;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FlightCrewMemberRequest(
        @NotNull
        @Positive
        Long crewMemberId,
        @NotBlank
        String crewRole
) {
}