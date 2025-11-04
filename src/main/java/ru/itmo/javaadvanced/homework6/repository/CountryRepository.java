package ru.itmo.javaadvanced.homework6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.javaadvanced.homework6.entity.Country;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByCode(String code);
    boolean existsByCode(String code);
}