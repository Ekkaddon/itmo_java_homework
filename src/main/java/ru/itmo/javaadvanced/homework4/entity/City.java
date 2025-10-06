package ru.itmo.javaadvanced.homework4.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class City {
    private Long id;
    private String code;
    private String nameRu;
    private String nameEn;
    private Long population;
    private Long regionId;

    public City(String code, String nameRu, String nameEn, Long population, Long regionId) {
        this.code = code;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.population = population;
        this.regionId = regionId;
    }
}