package ru.itmo.javaadvanced.homework6.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itmo.javaadvanced.homework6.dto.CityDto;
import ru.itmo.javaadvanced.homework6.entity.City;
import ru.itmo.javaadvanced.homework6.repository.CityRepository;
import ru.itmo.javaadvanced.homework6.repository.RegionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    void createCity_Success() {
        CityDto dto = new CityDto();
        dto.setCode("MSK");
        dto.setNameRu("Москва");
        dto.setNameEn("Moscow");
        dto.setPopulation(12500000L);

        City city = new City();
        city.setId(1L);
        city.setCode("MSK");

        when(cityRepository.existsByCode("MSK")).thenReturn(false);
        when(cityRepository.save(any(City.class))).thenReturn(city);

        CityDto result = cityService.createCity(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cityRepository).save(any(City.class));
    }

    @Test
    void createCity_DuplicateCode_ThrowsException() {
        CityDto dto = new CityDto();
        dto.setCode("MSK");

        when(cityRepository.existsByCode("MSK")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> cityService.createCity(dto));
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void getAllCities_ReturnsListOfCities() {
        City city1 = new City();
        city1.setId(1L);
        City city2 = new City();
        city2.setId(2L);

        when(cityRepository.findAll()).thenReturn(Arrays.asList(city1, city2));

        List<CityDto> result = cityService.getAllCities();

        assertEquals(2, result.size());
        verify(cityRepository).findAll();
    }

    @Test
    void deleteCity_NotFound_ThrowsException() {
        when(cityRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> cityService.deleteCity(999L));
        verify(cityRepository, never()).deleteById(any());
    }
}
