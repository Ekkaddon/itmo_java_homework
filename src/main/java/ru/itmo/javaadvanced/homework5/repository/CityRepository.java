package ru.itmo.javaadvanced.homework5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.javaadvanced.homework5.entity.City;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByCode(String code);
    boolean existsByCode(String code);
    List<City> findByRegionId(Long regionId);
}