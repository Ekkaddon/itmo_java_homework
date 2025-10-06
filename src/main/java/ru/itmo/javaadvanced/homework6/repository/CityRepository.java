package ru.itmo.javaadvanced.homework6.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.javaadvanced.homework6.entity.City;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByCode(String code);
    boolean existsByCode(String code);
    List<City> findByRegionId(Long regionId);
    Page<City> findByRegionId(Long regionId, Pageable pageable);
}