package ru.itmo.javaadvanced.diploma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.javaadvanced.diploma.domain.entity.CrewMember;
import ru.itmo.javaadvanced.diploma.domain.enums.CrewMemberType;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {

    List<CrewMember> findByCrewMemberType(CrewMemberType crewMemberType);

    Optional<CrewMember> findByEmail(String email);
}