package ru.itmo.javaadvanced.homework6.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.javaadvanced.homework6.dto.CountryDto;
import ru.itmo.javaadvanced.homework6.entity.Country;
import ru.itmo.javaadvanced.homework6.repository.CountryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Transactional
    public CountryDto createCountry(CountryDto dto) {
        if (countryRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Страна с кодом " + dto.getCode() + " уже существует");
        }
        Country country = new Country();
        country.setCode(dto.getCode());
        country.setNameRu(dto.getNameRu());
        country.setNameEn(dto.getNameEn());
        
        Country saved = countryRepository.save(country);
        return toDto(saved);
    }

    public List<CountryDto> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CountryDto getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Страна с ID " + id + " не найдена"));
        return toDto(country);
    }

    @Transactional
    public CountryDto updateCountry(Long id, CountryDto dto) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Страна с ID " + id + " не найдена"));
        
        country.setCode(dto.getCode());
        country.setNameRu(dto.getNameRu());
        country.setNameEn(dto.getNameEn());
        
        Country updated = countryRepository.save(country);
        return toDto(updated);
    }

    @Transactional
    public void deleteCountry(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new IllegalArgumentException("Страна с ID " + id + " не найдена");
        }
        countryRepository.deleteById(id);
    }

    private CountryDto toDto(Country country) {
        CountryDto dto = new CountryDto();
        dto.setId(country.getId());
        dto.setCode(country.getCode());
        dto.setNameRu(country.getNameRu());
        dto.setNameEn(country.getNameEn());
        return dto;
    }
}