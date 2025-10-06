package ru.itmo.javaadvanced.homework2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TemperatureConverterDemo implements CommandLineRunner {

    @Autowired
    private TemperatureConverterService temperatureConverterService;

    @Override
    public void run(String... args) {
        System.out.println("=== Конвертер температур ===");

        // Примеры конвертации из Цельсий в Кельвины и Фаренгейты
        double celsius = 25.0;
        System.out.printf("25°C = %.2f°K = %.2f°F%n", 
            temperatureConverterService.convert(celsius, TemperatureUnit.CELSIUS, TemperatureUnit.KELVIN),
            temperatureConverterService.convert(celsius, TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT));

        // Примеры конвертации из Кельвин в другие единицы
        double kelvin = 300.0;
        System.out.printf("300°K = %.2f°C = %.2f°F%n", 
            temperatureConverterService.convert(kelvin, TemperatureUnit.KELVIN, TemperatureUnit.CELSIUS),
            temperatureConverterService.convert(kelvin, TemperatureUnit.KELVIN, TemperatureUnit.FAHRENHEIT));

        // Примеры конвертации из Фаренгейт в другие единицы
        double fahrenheit = 77.0;
        System.out.printf("77°F = %.2f°C = %.2f°K%n", 
            temperatureConverterService.convert(fahrenheit, TemperatureUnit.FAHRENHEIT, TemperatureUnit.CELSIUS),
            temperatureConverterService.convert(fahrenheit, TemperatureUnit.FAHRENHEIT, TemperatureUnit.KELVIN));
    }
}