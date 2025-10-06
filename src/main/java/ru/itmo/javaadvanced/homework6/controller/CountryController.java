package ru.itmo.javaadvanced.homework6.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itmo.javaadvanced.homework6.dto.CountryDto;
import ru.itmo.javaadvanced.homework6.service.CountryService;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CountryDto createCountry(@Valid @RequestBody CountryDto dto) {
        return countryService.createCountry(dto);
    }

    @GetMapping
    public List<CountryDto> getAllCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/{id}")
    public CountryDto getCountryById(@PathVariable Long id) {
        return countryService.getCountryById(id);
    }

    @PutMapping("/{id}")
    public CountryDto updateCountry(@PathVariable Long id, @Valid @RequestBody CountryDto dto) {
        return countryService.updateCountry(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
    }
}