package ru.itmo.javaadvanced.homework6.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public Page<RegionDto> getAllRegions(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return regionService.getAllRegions(pageable);
    }

    @GetMapping("/{id}")
    public RegionDto getRegionById(@PathVariable Long id) {
        return regionService.getRegionById(id);
    }

    @GetMapping("/country/{countryId}")
    public Page<RegionDto> getRegionsByCountry(@PathVariable Long countryId, @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return regionService.getRegionsByCountry(countryId, pageable);
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