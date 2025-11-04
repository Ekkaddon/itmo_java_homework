package ru.itmo.javaadvanced.homework4.ui;

import org.springframework.stereotype.Component;
import ru.itmo.javaadvanced.homework4.entity.City;
import ru.itmo.javaadvanced.homework4.entity.Region;
import ru.itmo.javaadvanced.homework4.service.CityService;
import ru.itmo.javaadvanced.homework4.service.RegionService;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleUI {

    private final CityService cityService;
    private final RegionService regionService;
    private final Scanner scanner;

    public ConsoleUI(CityService cityService, RegionService regionService) {
        this.cityService = cityService;
        this.regionService = regionService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Справочник городов ===");
        
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            
            if (!processChoice(choice)) {
                break;
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Меню ---");
        System.out.println("1. Добавить регион");
        System.out.println("2. Добавить город");
        System.out.println("3. Показать все регионы");
        System.out.println("4. Показать все города");
        System.out.println("5. Показать города по региону");
        System.out.println("6. Обновить город");
        System.out.println("7. Удалить город");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private boolean processChoice(String choice) {
        switch (choice) {
            case "1" -> addRegion();
            case "2" -> addCity();
            case "3" -> viewAllRegions();
            case "4" -> viewAllCities();
            case "5" -> viewCitiesByRegion();
            case "6" -> updateCity();
            case "7" -> deleteCity();
            case "0" -> {
                System.out.println("Выход...");
                return false;
            }
            default -> System.out.println("Неверный выбор!");
        }
        return true;
    }

    private void addRegion() {
        try {
            System.out.print("Код региона: ");
            String code = scanner.nextLine().trim();
            
            System.out.print("Название региона (рус): ");
            String nameRu = scanner.nextLine().trim();
            
            System.out.print("Название региона (англ): ");
            String nameEn = scanner.nextLine().trim();
            
            Region region = regionService.createRegion(code, nameRu, nameEn);
            System.out.println("Регион добавлен: " + region);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void addCity() {
        try {
            System.out.print("Код города: ");
            String code = scanner.nextLine().trim();
            
            System.out.print("Название города (рус): ");
            String nameRu = scanner.nextLine().trim();
            
            System.out.print("Название города (англ): ");
            String nameEn = scanner.nextLine().trim();
            
            System.out.print("Численность населения: ");
            Long population = Long.parseLong(scanner.nextLine().trim());
            
            Long regionId = null;
            System.out.print("ID региона (или Enter для пропуска): ");
            String regionIdInput = scanner.nextLine().trim();
            if (!regionIdInput.isEmpty()) {
                regionId = Long.parseLong(regionIdInput);
            }
            
            City city = cityService.createCity(code, nameRu, nameEn, population, regionId);
            System.out.println("Город добавлен: " + city);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void viewAllRegions() {
        List<Region> regions = regionService.getAllRegions();
        System.out.println("\n=== Все регионы ===");
        if (regions.isEmpty()) {
            System.out.println("Регионы отсутствуют");
        } else {
            regions.forEach(System.out::println);
        }
    }

    private void viewAllCities() {
        List<City> cities = cityService.getAllCities();
        System.out.println("\n=== Все города ===");
        if (cities.isEmpty()) {
            System.out.println("Города отсутствуют");
        } else {
            cities.forEach(System.out::println);
        }
    }

    private void viewCitiesByRegion() {
        try {
            System.out.print("ID региона: ");
            Long regionId = Long.parseLong(scanner.nextLine().trim());
            
            List<City> cities = cityService.getCitiesByRegion(regionId);
            System.out.println("\n=== Города региона " + regionId + " ===");
            if (cities.isEmpty()) {
                System.out.println("Города отсутствуют");
            } else {
                cities.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void updateCity() {
        try {
            System.out.print("ID города для обновления: ");
            Long id = Long.parseLong(scanner.nextLine().trim());
            
            City city = cityService.getCityById(id);
            System.out.println("Текущие данные: " + city);
            
            System.out.print("Новый код (Enter для пропуска): ");
            String code = scanner.nextLine().trim();
            
            System.out.print("Новое название (рус, Enter для пропуска): ");
            String nameRu = scanner.nextLine().trim();
            
            System.out.print("Новое название (англ, Enter для пропуска): ");
            String nameEn = scanner.nextLine().trim();
            
            System.out.print("Новая численность (Enter для пропуска): ");
            String populationStr = scanner.nextLine().trim();
            Long population = populationStr.isEmpty() ? null : Long.parseLong(populationStr);
            
            System.out.print("Новый ID региона (Enter для пропуска): ");
            String regionIdStr = scanner.nextLine().trim();
            Long regionId = regionIdStr.isEmpty() ? null : Long.parseLong(regionIdStr);
            
            City updated = cityService.updateCity(id, code, nameRu, nameEn, population, regionId);
            System.out.println("Город обновлен: " + updated);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void deleteCity() {
        try {
            System.out.print("ID города для удаления: ");
            Long id = Long.parseLong(scanner.nextLine().trim());
            
            cityService.deleteCity(id);
            System.out.println("Город удален");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}