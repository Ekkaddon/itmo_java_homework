package ru.itmo.javaadvanced.homework2;

import lombok.Getter;

/**
 * Единицы измерения температуры
 */
@Getter
public enum TemperatureUnit {
    CELSIUS("C", "Цельсий"),
    KELVIN("K", "Кельвин"),
    FAHRENHEIT("F", "Фаренгейт");

    private final String symbol;
    private final String name;

    TemperatureUnit(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

}