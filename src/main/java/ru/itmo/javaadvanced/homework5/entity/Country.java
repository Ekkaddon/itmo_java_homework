package ru.itmo.javaadvanced.homework5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "regions")
public class Country {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 10)
    private String code;
    
    @Column(name = "name_ru", nullable = false, length = 100)
    private String nameRu;
    
    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;
    
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Region> regions = new ArrayList<>();
    
    public Country(String code, String nameRu, String nameEn) {
        this.code = code;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
    }
}