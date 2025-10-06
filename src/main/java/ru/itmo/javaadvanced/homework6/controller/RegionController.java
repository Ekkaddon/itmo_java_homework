package ru.itmo.javaadvanced.homework6.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itmo.javaadvanced.homework6.dto.RegionDto;
import ru.itmo.javaadvanced.homework6.service.RegionService;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {
    
    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegionDto createRegion(@Valid @RequestBody RegionDto dto) {
        return regionService.createRegion(dto);
    }

    @GetMapping
    public List<RegionDto> getAllRegions() {
        return regionService.getAllRegions();
    }

    @GetMapping("/{id}")
    public RegionDto getRegionById(@PathVariable Long id) {
        return regionService.getRegionById(id);
    }

    @GetMapping("/country/{countryId}")
    public List<RegionDto> getRegionsByCountry(@PathVariable Long countryId) {
        return regionService.getRegionsByCountry(countryId);
    }

    @PutMapping("/{id}")
    public RegionDto updateRegion(@PathVariable Long id, @Valid @RequestBody RegionDto dto) {
        return regionService.updateRegion(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
    }
}