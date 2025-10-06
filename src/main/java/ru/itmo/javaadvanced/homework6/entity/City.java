package ru.itmo.javaadvanced.homework6.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    
    @NotBlank(message = "Код города не может быть пустым")
    @Size(min = 2, max = 10, message = "Код города должен быть от 2 до 10 символов")
    @Column(unique = true, nullable = false, length = 10)
    private String code;
    
    @NotBlank(message = "Название города на русском не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    @Column(name = "name_ru", nullable = false, length = 100)
    private String nameRu;
    
    @NotBlank(message = "Название города на английском не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;
    
    @NotNull(message = "Численность населения обязательна")
    @Min(value = 1, message = "Численность населения должна быть больше 0")
    @Column(nullable = false)
    private Long population;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;
}