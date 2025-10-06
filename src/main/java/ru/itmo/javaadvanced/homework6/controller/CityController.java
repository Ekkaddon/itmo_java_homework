package ru.itmo.javaadvanced.homework6.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itmo.javaadvanced.homework6.dto.CityDto;
import ru.itmo.javaadvanced.homework6.service.CityService;

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
    public Page<CityDto> getAllCities(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return cityService.getAllCities(pageable);
    }

    @GetMapping("/{id}")
    public CityDto getCityById(@PathVariable Long id) {
        return cityService.getCityById(id);
    }

    @GetMapping("/region/{regionId}")
    public Page<CityDto> getCitiesByRegion(@PathVariable Long regionId, @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return cityService.getCitiesByRegion(regionId, pageable);
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