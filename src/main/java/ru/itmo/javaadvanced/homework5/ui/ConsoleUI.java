package ru.itmo.javaadvanced.homework5.ui;

import org.springframework.stereotype.Component;
import ru.itmo.javaadvanced.homework5.entity.City;
import ru.itmo.javaadvanced.homework5.entity.Country;
import ru.itmo.javaadvanced.homework5.entity.Region;
import ru.itmo.javaadvanced.homework5.service.CityService;
import ru.itmo.javaadvanced.homework5.service.CountryService;
import ru.itmo.javaadvanced.homework5.service.RegionService;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleUI {

    private final CityService cityService;
    private final RegionService regionService;
    private final CountryService countryService;
    private final Scanner scanner;

    public ConsoleUI(CityService cityService, RegionService regionService, CountryService countryService) {
        this.cityService = cityService;
        this.regionService = regionService;
        this.countryService = countryService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Справочник городов (JPA версия) ===");
        
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
        System.out.println("1. Добавить страну");
        System.out.println("2. Добавить регион");
        System.out.println("3. Добавить город");
        System.out.println("4. Показать все страны");
        System.out.println("5. Показать все регионы");
        System.out.println("6. Показать все города");
        System.out.println("7. Показать регионы по стране");
        System.out.println("8. Показать города по региону");
        System.out.println("9. Найти страну по коду");
        System.out.println("10. Найти город по коду");
        System.out.println("11. Обновить страну");
        System.out.println("12. Обновить регион");
        System.out.println("13. Обновить город");
        System.out.println("14. Удалить страну");
        System.out.println("15. Удалить регион");
        System.out.println("16. Удалить город");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private boolean processChoice(String choice) {
        try {
            switch (choice) {
                case "1" -> addCountry();
                case "2" -> addRegion();
                case "3" -> addCity();
                case "4" -> viewAllCountries();
                case "5" -> viewAllRegions();
                case "6" -> viewAllCities();
                case "7" -> viewRegionsByCountry();
                case "8" -> viewCitiesByRegion();
                case "9" -> findCountryByCode();
                case "10" -> findCityByCode();
                case "11" -> updateCountry();
                case "12" -> updateRegion();
                case "13" -> updateCity();
                case "14" -> deleteCountry();
                case "15" -> deleteRegion();
                case "16" -> deleteCity();
                case "0" -> {
                    System.out.println("Выход...");
                    return false;
                }
                default -> System.out.println("Неверный выбор!");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        return true;
    }

    private void addCountry() {
        System.out.print("Код страны: ");
        String code = scanner.nextLine().trim();
        
        System.out.print("Название страны (рус): ");
        String nameRu = scanner.nextLine().trim();
        
        System.out.print("Название страны (англ): ");
        String nameEn = scanner.nextLine().trim();
        
        Country country = countryService.createCountry(code, nameRu, nameEn);
        System.out.println("Страна добавлена: " + country);
    }

    private void addRegion() {
        System.out.print("Код региона: ");
        String code = scanner.nextLine().trim();
        
        System.out.print("Название региона (рус): ");
        String nameRu = scanner.nextLine().trim();
        
        System.out.print("Название региона (англ): ");
        String nameEn = scanner.nextLine().trim();
        
        Long countryId = null;
        System.out.print("ID страны (или Enter для пропуска): ");
        String countryIdInput = scanner.nextLine().trim();
        if (!countryIdInput.isEmpty()) {
            countryId = Long.parseLong(countryIdInput);
        }
        
        Region region = regionService.createRegion(code, nameRu, nameEn, countryId);
        System.out.println("Регион добавлен: " + region);
    }

    private void addCity() {
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
    }

    private void viewAllCountries() {
        List<Country> countries = countryService.getAllCountries();
        System.out.println("\n=== Все страны ===");
        if (countries.isEmpty()) {
            System.out.println("Страны отсутствуют");
        } else {
            countries.forEach(System.out::println);
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

    private void viewRegionsByCountry() {
        System.out.print("ID страны: ");
        Long countryId = Long.parseLong(scanner.nextLine().trim());
        
        List<Region> regions = regionService.getRegionsByCountry(countryId);
        System.out.println("\n=== Регионы страны " + countryId + " ===");
        if (regions.isEmpty()) {
            System.out.println("Регионы отсутствуют");
        } else {
            regions.forEach(System.out::println);
        }
    }

    private void viewCitiesByRegion() {
        System.out.print("ID региона: ");
        Long regionId = Long.parseLong(scanner.nextLine().trim());
        
        List<City> cities = cityService.getCitiesByRegion(regionId);
        System.out.println("\n=== Города региона " + regionId + " ===");
        if (cities.isEmpty()) {
            System.out.println("Города отсутствуют");
        } else {
            cities.forEach(System.out::println);
        }
    }

    private void updateCity() {
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
    }

    private void deleteCity() {
        System.out.print("ID города для удаления: ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        
        cityService.deleteCity(id);
        System.out.println("Город удален");
    }

    private void updateCountry() {
        System.out.print("ID страны для обновления: ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        
        Country country = countryService.getCountryById(id);
        System.out.println("Текущие данные: " + country);
        
        System.out.print("Новый код (Enter для пропуска): ");
        String code = scanner.nextLine().trim();
        
        System.out.print("Новое название (рус, Enter для пропуска): ");
        String nameRu = scanner.nextLine().trim();
        
        System.out.print("Новое название (англ, Enter для пропуска): ");
        String nameEn = scanner.nextLine().trim();
        
        Country updated = countryService.updateCountry(id, code, nameRu, nameEn);
        System.out.println("Страна обновлена: " + updated);
    }

    private void deleteCountry() {
        System.out.print("ID страны для удаления: ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        
        countryService.deleteCountry(id);
        System.out.println("Страна удалена");
    }

    private void updateRegion() {
        System.out.print("ID региона для обновления: ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        
        Region region = regionService.getRegionById(id);
        System.out.println("Текущие данные: " + region);
        
        System.out.print("Новый код (Enter для пропуска): ");
        String code = scanner.nextLine().trim();
        
        System.out.print("Новое название (рус, Enter для пропуска): ");
        String nameRu = scanner.nextLine().trim();
        
        System.out.print("Новое название (англ, Enter для пропуска): ");
        String nameEn = scanner.nextLine().trim();
        
        System.out.print("Новый ID страны (Enter для пропуска): ");
        String countryIdStr = scanner.nextLine().trim();
        Long countryId = countryIdStr.isEmpty() ? null : Long.parseLong(countryIdStr);
        
        Region updated = regionService.updateRegion(id, code, nameRu, nameEn, countryId);
        System.out.println("Регион обновлен: " + updated);
    }

    private void deleteRegion() {
        System.out.print("ID региона для удаления: ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        
        regionService.deleteRegion(id);
        System.out.println("Регион удален");
    }

    private void findCountryByCode() {
        System.out.print("Введите код страны: ");
        String code = scanner.nextLine().trim();
        
        Country country = countryService.getCountryByCode(code);
        if (country != null) {
            System.out.println("\n=== Найдена страна ===");
            System.out.println(country);
        } else {
            System.out.println("Страна с кодом '" + code + "' не найдена");
        }
    }

    private void findCityByCode() {
        System.out.print("Введите код города: ");
        String code = scanner.nextLine().trim();
        
        City city = cityService.getCityByCode(code);
        if (city != null) {
            System.out.println("\n=== Найден город ===");
            System.out.println(city);
        } else {
            System.out.println("Город с кодом '" + code + "' не найден");
        }
    }
}