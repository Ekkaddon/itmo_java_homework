package ru.itmo.javaadvanced.homework4.service;

import org.springframework.stereotype.Service;
import ru.itmo.javaadvanced.homework4.dao.CityDao;
import ru.itmo.javaadvanced.homework4.dao.RegionDao;
import ru.itmo.javaadvanced.homework4.entity.City;
import ru.itmo.javaadvanced.homework4.entity.Region;

import java.util.List;

@Service
public class CityService {
    private final CityDao cityDao;
    private final RegionDao regionDao;

    public CityService(CityDao cityDao, RegionDao regionDao) {
        this.cityDao = cityDao;
        this.regionDao = regionDao;
    }

    public City createCity(String code, String nameRu, String nameEn, Long population, Long regionId) {
        City existing = cityDao.findByCode(code);
        if (existing != null) {
            throw new IllegalArgumentException("Город с кодом " + code + " уже существует");
        }
        
        if (regionId != null) {
            Region region = regionDao.findById(regionId);
            if (region == null) {
                throw new IllegalArgumentException("Регион с ID " + regionId + " не найден");
            }
        }
        
        City city = new City(code, nameRu, nameEn, population, regionId);
        return cityDao.save(city);
    }

    public List<City> getAllCities() {
        return cityDao.findAll();
    }

    public City getCityById(Long id) {
        City city = cityDao.findById(id);
        if (city == null) {
            throw new IllegalArgumentException("Город с ID " + id + " не найден");
        }
        return city;
    }

    public City getCityByCode(String code) {
        return cityDao.findByCode(code);
    }

    public List<City> getCitiesByRegion(Long regionId) {
        Region region = regionDao.findById(regionId);
        if (region == null) {
            throw new IllegalArgumentException("Регион с ID " + regionId + " не найден");
        }
        return cityDao.findByRegionId(regionId);
    }

    public City updateCity(Long id, String code, String nameRu, String nameEn, Long population, Long regionId) {
        City city = getCityById(id);
        
        if (code != null && !code.isEmpty()) {
            city.setCode(code);
        }
        if (nameRu != null && !nameRu.isEmpty()) {
            city.setNameRu(nameRu);
        }
        if (nameEn != null && !nameEn.isEmpty()) {
            city.setNameEn(nameEn);
        }
        if (population != null) {
            city.setPopulation(population);
        }
        if (regionId != null) {
            Region region = regionDao.findById(regionId);
            if (region == null) {
                throw new IllegalArgumentException("Регион с ID " + regionId + " не найден");
            }
            city.setRegionId(regionId);
        }
        
        cityDao.update(city);
        return city;
    }

    public void deleteCity(Long id) {
        getCityById(id);
        cityDao.deleteById(id);
    }
}