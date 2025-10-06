package ru.itmo.javaadvanced.homework6.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CityDto {
    private Long id;
    
    @NotBlank(message = "Код города не может быть пустым")
    @Size(min = 2, max = 10, message = "Код города должен быть от 2 до 10 символов")
    private String code;
    
    @NotBlank(message = "Название города на русском не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String nameRu;
    
    @NotBlank(message = "Название города на английском не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String nameEn;
    
    @NotNull(message = "Численность населения обязательна")
    @Min(value = 1, message = "Численность населения должна быть больше 0")
    private Long population;
    
    private Long regionId;
}