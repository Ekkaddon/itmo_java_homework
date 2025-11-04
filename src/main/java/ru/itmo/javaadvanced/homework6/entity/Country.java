package ru.itmo.javaadvanced.homework6.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    
    @NotBlank(message = "Код страны не может быть пустым")
    @Size(min = 2, max = 10, message = "Код страны должен быть от 2 до 10 символов")
    @Column(unique = true, nullable = false, length = 10)
    private String code;
    
    @NotBlank(message = "Название страны на русском не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    @Column(name = "name_ru", nullable = false, length = 100)
    private String nameRu;
    
    @NotBlank(message = "Название страны на английском не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;
    
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Region> regions = new ArrayList<>();
}