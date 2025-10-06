package ru.itmo.javaadvanced.homework6.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itmo.javaadvanced.homework6.dto.CityDto;
import ru.itmo.javaadvanced.homework6.service.CityService;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CityDto createCity(@Valid @RequestBody CityDto dto) {
        return cityService.createCity(dto);
    }

    @GetMapping
    public List<CityDto> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/{id}")
    public CityDto getCityById(@PathVariable Long id) {
        return cityService.getCityById(id);
    }

    @GetMapping("/region/{regionId}")
    public List<CityDto> getCitiesByRegion(@PathVariable Long regionId) {
        return cityService.getCitiesByRegion(regionId);
    }

    @PutMapping("/{id}")
    public CityDto updateCity(@PathVariable Long id, @Valid @RequestBody CityDto dto) {
        return cityService.updateCity(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
    }
}