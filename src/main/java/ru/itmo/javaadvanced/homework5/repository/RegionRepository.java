package ru.itmo.javaadvanced.homework5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.javaadvanced.homework5.entity.Region;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByCode(String code);
    boolean existsByCode(String code);
    List<Region> findByCountryId(Long countryId);
}