package ru.itmo.javaadvanced.homework6.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    
    @NotBlank(message = "Код региона не может быть пустым")
    @Size(min = 2, max = 10, message = "Код региона должен быть от 2 до 10 символов")
    @Column(unique = true, nullable = false, length = 10)
    private String code;
    
    @NotBlank(message = "Название региона на русском не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    @Column(name = "name_ru", nullable = false, length = 100)
    private String nameRu;
    
    @NotBlank(message = "Название региона на английском не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;
    
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<City> cities = new ArrayList<>();
}