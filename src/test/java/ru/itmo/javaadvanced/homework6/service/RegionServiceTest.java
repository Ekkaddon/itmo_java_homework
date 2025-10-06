package ru.itmo.javaadvanced.homework6.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itmo.javaadvanced.homework6.dto.RegionDto;
import ru.itmo.javaadvanced.homework6.entity.Region;
import ru.itmo.javaadvanced.homework6.repository.CountryRepository;
import ru.itmo.javaadvanced.homework6.repository.RegionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private RegionService regionService;

    @Test
    void createRegion_Success() {
        RegionDto dto = new RegionDto();
        dto.setCode("77");
        dto.setNameRu("Москва");
        dto.setNameEn("Moscow");

        Region region = new Region();
        region.setId(1L);
        region.setCode("77");

        when(regionRepository.existsByCode("77")).thenReturn(false);
        when(regionRepository.save(any(Region.class))).thenReturn(region);

        RegionDto result = regionService.createRegion(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(regionRepository).save(any(Region.class));
    }

    @Test
    void getAllRegions_ReturnsListOfRegions() {
        Region region1 = new Region();
        region1.setId(1L);
        Region region2 = new Region();
        region2.setId(2L);

        when(regionRepository.findAll()).thenReturn(Arrays.asList(region1, region2));

        List<RegionDto> result = regionService.getAllRegions();

        assertEquals(2, result.size());
        verify(regionRepository).findAll();
    }

    @Test
    void getRegionById_NotFound_ThrowsException() {
        when(regionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> regionService.getRegionById(999L));
    }

    @Test
    void deleteRegion_Success() {
        when(regionRepository.existsById(1L)).thenReturn(true);

        regionService.deleteRegion(1L);

        verify(regionRepository).deleteById(1L);
    }
}
