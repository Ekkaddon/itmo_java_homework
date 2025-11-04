package ru.itmo.javaadvanced.diploma.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.javaadvanced.diploma.domain.entity.CrewMember;
import ru.itmo.javaadvanced.diploma.domain.entity.UserAccount;
import ru.itmo.javaadvanced.diploma.dto.profile.ProfileResponse;
import ru.itmo.javaadvanced.diploma.exception.ResourceNotFoundException;
import ru.itmo.javaadvanced.diploma.repository.UserAccountRepository;
import ru.itmo.javaadvanced.diploma.security.UserAccountDetails;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final UserAccountRepository userAccountRepository;

    public ProfileResponse getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserAccountDetails userDetails)) {
            throw new IllegalStateException("Authenticated user is required");
        }

        UserAccount userAccount = userAccountRepository.findById(userDetails.userAccount().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CrewMember crewMember = userAccount.getCrewMember();
        if (crewMember == null) {
            throw new IllegalStateException("User has no associated crew member");
        }

        String fullName = crewMember.getLastName() + " " + crewMember.getFirstName()
                + (crewMember.getMiddleName() != null ? " " + crewMember.getMiddleName() : "");

        return new ProfileResponse(
                userAccount.getId(),
                crewMember.getId(),
                crewMember.getCrewMemberType(),
                fullName.trim(),
                crewMember.getEmail(),
                crewMember.getPhoneNumber(),
                crewMember.getLicenseNumber(),
                crewMember.getExperienceYears()
        );
    }
}