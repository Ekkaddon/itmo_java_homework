package ru.itmo.javaadvanced.homework2;

import org.springframework.stereotype.Service;

/**
 * Сервис для конвертации температур между различными единицами измерения
 * Реализован как Singleton bean
 */
@Service
public class TemperatureConverterService {

    public double convert(double value, TemperatureUnit fromUnit, TemperatureUnit toUnit) {
        if (fromUnit == toUnit) {
            return value;
        }

        double celsius = convertToCelsius(value, fromUnit);
        return convertFromCelsius(celsius, toUnit);
    }

    /**
     * Конвертирует температуру в Цельсии
     */
    private double convertToCelsius(double value, TemperatureUnit fromUnit) {
        return switch (fromUnit) {
            case CELSIUS -> value;
            case KELVIN -> value - 273.15;
            case FAHRENHEIT -> (value - 32) * 5.0 / 9.0;
        };
    }

    /**
     * Конвертирует температуру из Цельсий
     */
    private double convertFromCelsius(double celsius, TemperatureUnit toUnit) {
        return switch (toUnit) {
            case CELSIUS -> celsius;
            case KELVIN -> celsius + 273.15;
            case FAHRENHEIT -> celsius * 9.0 / 5.0 + 32;
        };
    }
}