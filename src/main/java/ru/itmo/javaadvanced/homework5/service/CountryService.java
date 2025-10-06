package ru.itmo.javaadvanced.homework5.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.javaadvanced.homework5.entity.Country;
import ru.itmo.javaadvanced.homework5.repository.CountryRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Transactional
    public Country createCountry(String code, String nameRu, String nameEn) {
        if (countryRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Страна с кодом " + code + " уже существует");
        }
        Country country = new Country(code, nameRu, nameEn);
        return countryRepository.save(country);
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Country getCountryById(Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Страна с ID " + id + " не найдена"));
    }

    public Country getCountryByCode(String code) {
        return countryRepository.findByCode(code).orElse(null);
    }

    @Transactional
    public Country updateCountry(Long id, String code, String nameRu, String nameEn) {
        Country country = getCountryById(id);
        if (code != null && !code.isEmpty()) {
            country.setCode(code);
        }
        if (nameRu != null && !nameRu.isEmpty()) {
            country.setNameRu(nameRu);
        }
        if (nameEn != null && !nameEn.isEmpty()) {
            country.setNameEn(nameEn);
        }
        return countryRepository.save(country);
    }

    @Transactional
    public void deleteCountry(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new IllegalArgumentException("Страна с ID " + id + " не найдена");
        }
        countryRepository.deleteById(id);
    }
}