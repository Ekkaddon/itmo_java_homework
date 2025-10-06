package ru.itmo.javaadvanced.homework6.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.javaadvanced.homework6.dto.CityDto;
import ru.itmo.javaadvanced.homework6.entity.City;
import ru.itmo.javaadvanced.homework6.entity.Region;
import ru.itmo.javaadvanced.homework6.repository.CityRepository;
import ru.itmo.javaadvanced.homework6.repository.RegionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CityService {
    private final CityRepository cityRepository;
    private final RegionRepository regionRepository;

    public CityService(CityRepository cityRepository, RegionRepository regionRepository) {
        this.cityRepository = cityRepository;
        this.regionRepository = regionRepository;
    }

    @Transactional
    public CityDto createCity(CityDto dto) {
        if (cityRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Город с кодом " + dto.getCode() + " уже существует");
        }
        
        Region region = null;
        if (dto.getRegionId() != null) {
            region = regionRepository.findById(dto.getRegionId())
                    .orElseThrow(() -> new IllegalArgumentException("Регион с ID " + dto.getRegionId() + " не найден"));
        }
        
        City city = new City();
        city.setCode(dto.getCode());
        city.setNameRu(dto.getNameRu());
        city.setNameEn(dto.getNameEn());
        city.setPopulation(dto.getPopulation());
        city.setRegion(region);
        
        City saved = cityRepository.save(city);
        return toDto(saved);
    }

    public List<CityDto> getAllCities() {
        return cityRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CityDto getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Город с ID " + id + " не найден"));
        return toDto(city);
    }

    public List<CityDto> getCitiesByRegion(Long regionId) {
        if (!regionRepository.existsById(regionId)) {
            throw new IllegalArgumentException("Регион с ID " + regionId + " не найден");
        }
        return cityRepository.findByRegionId(regionId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CityDto updateCity(Long id, CityDto dto) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Город с ID " + id + " не найден"));
        
        city.setCode(dto.getCode());
        city.setNameRu(dto.getNameRu());
        city.setNameEn(dto.getNameEn());
        city.setPopulation(dto.getPopulation());
        
        if (dto.getRegionId() != null) {
            Region region = regionRepository.findById(dto.getRegionId())
                    .orElseThrow(() -> new IllegalArgumentException("Регион с ID " + dto.getRegionId() + " не найден"));
            city.setRegion(region);
        } else {
            city.setRegion(null);
        }
        
        City updated = cityRepository.save(city);
        return toDto(updated);
    }

    @Transactional
    public void deleteCity(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new IllegalArgumentException("Город с ID " + id + " не найден");
        }
        cityRepository.deleteById(id);
    }

    private CityDto toDto(City city) {
        CityDto dto = new CityDto();
        dto.setId(city.getId());
        dto.setCode(city.getCode());
        dto.setNameRu(city.getNameRu());
        dto.setNameEn(city.getNameEn());
        dto.setPopulation(city.getPopulation());
        dto.setRegionId(city.getRegion() != null ? city.getRegion().getId() : null);
        return dto;
    }
}