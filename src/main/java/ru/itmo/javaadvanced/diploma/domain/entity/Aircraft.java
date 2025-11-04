package ru.itmo.javaadvanced.diploma.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "aircrafts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_number", nullable = false, unique = true, length = 50)
    private String registrationNumber;

    @Column(name = "aircraft_model", nullable = false, length = 100)
    private String aircraftModel;

    @Column(name = "manufacturing_year")
    private Integer manufacturingYear;

    @Column(name = "seating_capacity", nullable = false)
    private Integer seatingCapacity;

    @Column(name = "cargo_capacity_kg")
    private Integer cargoCapacityKg;

    @Builder.Default
    @OneToMany(mappedBy = "aircraft", fetch = FetchType.LAZY)
    private Set<Flight> flights = new HashSet<>();
}