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
        if (regionDao.findByCode(code).isPresent()) {
            throw new IllegalArgumentException("Регион с кодом " + code + " уже существует");
        }
        Region region = new Region(code, nameRu, nameEn);
        return regionDao.save(region);
    }

    public List<Region> getAllRegions() {
        return regionDao.findAll();
    }

    public Region getRegionById(Long id) {
        return regionDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Регион с ID " + id + " не найден"));
    }

    public Region getRegionByCode(String code) {
        return regionDao.findByCode(code).orElse(null);
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