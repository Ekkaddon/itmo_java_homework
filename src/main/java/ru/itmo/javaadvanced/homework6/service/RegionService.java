package ru.itmo.javaadvanced.homework6.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.javaadvanced.homework6.dto.RegionDto;
import ru.itmo.javaadvanced.homework6.entity.Country;
import ru.itmo.javaadvanced.homework6.entity.Region;
import ru.itmo.javaadvanced.homework6.repository.CountryRepository;
import ru.itmo.javaadvanced.homework6.repository.RegionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionService {
    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;

    public RegionService(RegionRepository regionRepository, CountryRepository countryRepository) {
        this.regionRepository = regionRepository;
        this.countryRepository = countryRepository;
    }

    @Transactional
    public RegionDto createRegion(RegionDto dto) {
        if (regionRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Регион с кодом " + dto.getCode() + " уже существует");
        }
        
        Country country = null;
        if (dto.getCountryId() != null) {
            country = countryRepository.findById(dto.getCountryId())
                    .orElseThrow(() -> new IllegalArgumentException("Страна с ID " + dto.getCountryId() + " не найдена"));
        }
        
        Region region = new Region();
        region.setCode(dto.getCode());
        region.setNameRu(dto.getNameRu());
        region.setNameEn(dto.getNameEn());
        region.setCountry(country);
        
        Region saved = regionRepository.save(region);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public Page<RegionDto> getAllRegions(Pageable pageable) {
        return regionRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public RegionDto getRegionById(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Регион с ID " + id + " не найден"));
        return toDto(region);
    }

    @Transactional(readOnly = true)
    public Page<RegionDto> getRegionsByCountry(Long countryId, Pageable pageable) {
        if (!countryRepository.existsById(countryId)) {
            throw new IllegalArgumentException("Страна с ID " + countryId + " не найдена");
        }
        return regionRepository.findByCountryId(countryId, pageable).map(this::toDto);
    }

    @Transactional
    public RegionDto updateRegion(Long id, RegionDto dto) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Регион с ID " + id + " не найден"));
        
        region.setCode(dto.getCode());
        region.setNameRu(dto.getNameRu());
        region.setNameEn(dto.getNameEn());
        
        if (dto.getCountryId() != null) {
            Country country = countryRepository.findById(dto.getCountryId())
                    .orElseThrow(() -> new IllegalArgumentException("Страна с ID " + dto.getCountryId() + " не найдена"));
            region.setCountry(country);
        } else {
            region.setCountry(null);
        }
        
        Region updated = regionRepository.save(region);
        return toDto(updated);
    }

    @Transactional
    public void deleteRegion(Long id) {
        if (!regionRepository.existsById(id)) {
            throw new IllegalArgumentException("Регион с ID " + id + " не найден");
        }
        regionRepository.deleteById(id);
    }

    private RegionDto toDto(Region region) {
        RegionDto dto = new RegionDto();
        dto.setId(region.getId());
        dto.setCode(region.getCode());
        dto.setNameRu(region.getNameRu());
        dto.setNameEn(region.getNameEn());
        dto.setCountryId(region.getCountry() != null ? region.getCountry().getId() : null);
        return dto;
    }
}