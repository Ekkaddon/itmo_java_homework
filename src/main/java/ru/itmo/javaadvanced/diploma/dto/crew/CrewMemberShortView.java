package ru.itmo.javaadvanced.diploma.dto.crew;

public record CrewMemberShortView(
        Long id,
        String fullName,
        String phoneNumber
) {
}