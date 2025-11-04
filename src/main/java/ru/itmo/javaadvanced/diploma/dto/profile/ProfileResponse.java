package ru.itmo.javaadvanced.diploma.dto.profile;

import ru.itmo.javaadvanced.diploma.domain.enums.CrewMemberType;

public record ProfileResponse(
        Long userId,
        Long crewMemberId,
        CrewMemberType crewMemberType,
        String fullName,
        String email,
        String phoneNumber,
        String licenseNumber,
        Integer experienceYears
) {
}