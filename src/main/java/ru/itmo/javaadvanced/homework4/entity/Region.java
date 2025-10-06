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
public class Region {
    private Long id;
    private String code;
    private String nameRu;
    private String nameEn;

    public Region(String code, String nameRu, String nameEn) {
        this.code = code;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
    }
}