package ru.itmo.javaadvanced.homework5.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "regions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"country", "cities"})
public class Region {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 10)
    private String code;
    
    @Column(name = "name_ru", nullable = false, length = 100)
    private String nameRu;
    
    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;
    
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<City> cities = new ArrayList<>();
    
    public Region(String code, String nameRu, String nameEn, Country country) {
        this.code = code;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.country = country;
    }
}