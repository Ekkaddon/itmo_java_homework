package ru.itmo.javaadvanced.homework6.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CountryDto {
    private Long id;
    
    @NotBlank(message = "Код страны не может быть пустым")
    @Size(min = 2, max = 10, message = "Код страны должен быть от 2 до 10 символов")
    private String code;
    
    @NotBlank(message = "Название страны на русском не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String nameRu;
    
    @NotBlank(message = "Название страны на английском не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String nameEn;
}