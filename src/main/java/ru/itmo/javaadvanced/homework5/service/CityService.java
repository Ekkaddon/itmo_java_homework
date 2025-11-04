package ru.itmo.javaadvanced.homework5.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.javaadvanced.homework5.entity.City;
import ru.itmo.javaadvanced.homework5.entity.Region;
import ru.itmo.javaadvanced.homework5.repository.CityRepository;
import ru.itmo.javaadvanced.homework5.repository.RegionRepository;

import java.util.List;

@Service
public class CityService {
    private final CityRepository cityRepository;
    private final RegionRepository regionRepository;

    public CityService(CityRepository cityRepository, RegionRepository regionRepository) {
        this.cityRepository = cityRepository;
        this.regionRepository = regionRepository;
    }

    @Transactional
    public City createCity(String code, String nameRu, String nameEn, Long population, Long regionId) {
        if (cityRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Город с кодом " + code + " уже существует");
        }
        
        Region region = null;
        if (regionId != null) {
            region = regionRepository.findById(regionId)
                    .orElseThrow(() -> new IllegalArgumentException("Регион с ID " + regionId + " не найден"));
        }
        
        City city = new City(code, nameRu, nameEn, population, region);
        return cityRepository.save(city);
    }

    @Transactional(readOnly = true)
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public City getCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Город с ID " + id + " не найден"));
    }

    @Transactional(readOnly = true)
    public City getCityByCode(String code) {
        return cityRepository.findByCode(code).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<City> getCitiesByRegion(Long regionId) {
        if (!regionRepository.existsById(regionId)) {
            throw new IllegalArgumentException("Регион с ID " + regionId + " не найден");
        }
        return cityRepository.findByRegionId(regionId);
    }

    @Transactional
    public City updateCity(Long id, String code, String nameRu, String nameEn, Long population, Long regionId) {
        City city = getCityById(id);
        
        if (code != null && !code.isEmpty()) {
            city.setCode(code);
        }
        if (nameRu != null && !nameRu.isEmpty()) {
            city.setNameRu(nameRu);
        }
        if (nameEn != null && !nameEn.isEmpty()) {
            city.setNameEn(nameEn);
        }
        if (population != null) {
            city.setPopulation(population);
        }
        if (regionId != null) {
            Region region = regionRepository.findById(regionId)
                    .orElseThrow(() -> new IllegalArgumentException("Регион с ID " + regionId + " не найден"));
            city.setRegion(region);
        }
        
        return cityRepository.save(city);
    }

    @Transactional
    public void deleteCity(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new IllegalArgumentException("Город с ID " + id + " не найден");
        }
        cityRepository.deleteById(id);
    }
}