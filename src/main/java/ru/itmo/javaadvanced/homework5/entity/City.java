package ru.itmo.javaadvanced.homework5.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "region")
public class City {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 10)
    private String code;
    
    @Column(name = "name_ru", nullable = false, length = 100)
    private String nameRu;
    
    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;
    
    @Column(nullable = false)
    private Long population;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;
    
    public City(String code, String nameRu, String nameEn, Long population, Region region) {
        this.code = code;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.population = population;
        this.region = region;
    }
}