package ru.itmo.javaadvanced.homework4.service;

import org.springframework.stereotype.Service;
import ru.itmo.javaadvanced.homework4.dao.RegionDao;
import ru.itmo.javaadvanced.homework4.entity.Region;

import java.util.List;

@Service
public class RegionService {
    private final RegionDao regionDao;

    public RegionService(RegionDao regionDao) {
        this.regionDao = regionDao;
    }

    public Region createRegion(String code, String nameRu, String nameEn) {
        Region existing = regionDao.findByCode(code);
        if (existing != null) {
            throw new IllegalArgumentException("Регион с кодом " + code + " уже существует");
        }
        Region region = new Region(code, nameRu, nameEn);
        return regionDao.save(region);
    }

    public List<Region> getAllRegions() {
        return regionDao.findAll();
    }

    public Region getRegionById(Long id) {
        Region region = regionDao.findById(id);
        if (region == null) {
            throw new IllegalArgumentException("Регион с ID " + id + " не найден");
        }
        return region;
    }

    public Region getRegionByCode(String code) {
        return regionDao.findByCode(code);
    }

    public Region updateRegion(Long id, String code, String nameRu, String nameEn) {
        Region region = getRegionById(id);
        if (code != null && !code.isEmpty()) {
            region.setCode(code);
        }
        if (nameRu != null && !nameRu.isEmpty()) {
            region.setNameRu(nameRu);
        }
        if (nameEn != null && !nameEn.isEmpty()) {
            region.setNameEn(nameEn);
        }
        regionDao.update(region);
        return region;
    }

    public void deleteRegion(Long id) {
        getRegionById(id);
        regionDao.deleteById(id);
    }
}