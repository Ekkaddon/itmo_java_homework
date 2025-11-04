package ru.itmo.javaadvanced.homework6.dto;

import jakarta.validation.constraints.NotBlank;
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
public class RegionDto {
    private Long id;
    
    @NotBlank(message = "Код региона не может быть пустым")
    @Size(min = 2, max = 10, message = "Код региона должен быть от 2 до 10 символов")
    private String code;
    
    @NotBlank(message = "Название региона на русском не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String nameRu;
    
    @NotBlank(message = "Название региона на английском не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String nameEn;
    
    private Long countryId;
}