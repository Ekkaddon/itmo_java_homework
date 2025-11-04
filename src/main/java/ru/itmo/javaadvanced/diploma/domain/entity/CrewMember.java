package ru.itmo.javaadvanced.diploma.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.itmo.javaadvanced.diploma.domain.enums.CrewMemberType;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "crew_members")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "crew_member_type", nullable = false, length = 30)
    private CrewMemberType crewMemberType;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 30)
    private String phoneNumber;

    @Column(name = "license_number", length = 50)
    private String licenseNumber;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Builder.Default
    @OneToMany(mappedBy = "crewMember", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FlightCrew> crewAssignments = new LinkedHashSet<>();
}