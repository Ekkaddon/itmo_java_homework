package ru.itmo.javaadvanced.homework5.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.javaadvanced.homework5.entity.Country;
import ru.itmo.javaadvanced.homework5.entity.Region;
import ru.itmo.javaadvanced.homework5.repository.CountryRepository;
import ru.itmo.javaadvanced.homework5.repository.RegionRepository;

import java.util.List;

@Service
public class RegionService {
    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;

    public RegionService(RegionRepository regionRepository, CountryRepository countryRepository) {
        this.regionRepository = regionRepository;
        this.countryRepository = countryRepository;
    }

    @Transactional
    public Region createRegion(String code, String nameRu, String nameEn, Long countryId) {
        if (regionRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Регион с кодом " + code + " уже существует");
        }
        
        Country country = null;
        if (countryId != null) {
            country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new IllegalArgumentException("Страна с ID " + countryId + " не найдена"));
        }
        
        Region region = new Region(code, nameRu, nameEn, country);
        return regionRepository.save(region);
    }

    @Transactional(readOnly = true)
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Region getRegionById(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Регион с ID " + id + " не найден"));
    }

    @Transactional(readOnly = true)
    public List<Region> getRegionsByCountry(Long countryId) {
        if (!countryRepository.existsById(countryId)) {
            throw new IllegalArgumentException("Страна с ID " + countryId + " не найдена");
        }
        return regionRepository.findByCountryId(countryId);
    }

    @Transactional
    public Region updateRegion(Long id, String code, String nameRu, String nameEn, Long countryId) {
        Region region = getRegionById(id);
        
        if (code != null && !code.isEmpty()) {
            region.setCode(code);
        }
        if (nameRu != null && !nameRu.isEmpty()) {
            region.setNameRu(nameRu);
        }
        if (nameEn != null && !nameEn.isEmpty()) {
            region.setNameEn(nameEn);
        }
        if (countryId != null) {
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new IllegalArgumentException("Страна с ID " + countryId + " не найдена"));
            region.setCountry(country);
        }
        
        return regionRepository.save(region);
    }

    @Transactional
    public void deleteRegion(Long id) {
        if (!regionRepository.existsById(id)) {
            throw new IllegalArgumentException("Регион с ID " + id + " не найден");
        }
        regionRepository.deleteById(id);
    }
}