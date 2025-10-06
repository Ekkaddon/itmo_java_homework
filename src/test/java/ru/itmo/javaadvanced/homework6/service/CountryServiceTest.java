package ru.itmo.javaadvanced.homework6.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itmo.javaadvanced.homework6.dto.CountryDto;
import ru.itmo.javaadvanced.homework6.entity.Country;
import ru.itmo.javaadvanced.homework6.repository.CountryRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    @Test
    void createCountry_Success() {
        CountryDto dto = new CountryDto();
        dto.setCode("RU");
        dto.setNameRu("Россия");
        dto.setNameEn("Russia");

        Country country = new Country();
        country.setId(1L);
        country.setCode("RU");

        when(countryRepository.existsByCode("RU")).thenReturn(false);
        when(countryRepository.save(any(Country.class))).thenReturn(country);

        CountryDto result = countryService.createCountry(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(countryRepository).save(any(Country.class));
    }

    @Test
    void getAllCountries_ReturnsListOfCountries() {
        Country country1 = new Country();
        country1.setId(1L);
        Country country2 = new Country();
        country2.setId(2L);

        when(countryRepository.findAll()).thenReturn(Arrays.asList(country1, country2));

        List<CountryDto> result = countryService.getAllCountries();

        assertEquals(2, result.size());
        verify(countryRepository).findAll();
    }

    @Test
    void getCountryById_NotFound_ThrowsException() {
        when(countryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> countryService.getCountryById(999L));
    }

    @Test
    void deleteCountry_Success() {
        when(countryRepository.existsById(1L)).thenReturn(true);

        countryService.deleteCountry(1L);

        verify(countryRepository).deleteById(1L);
    }
}

