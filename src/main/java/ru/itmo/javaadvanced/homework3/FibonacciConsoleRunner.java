package ru.itmo.javaadvanced.homework3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Консольный интерфейс для работы с сервисом Fibonacci
 */
@Component
public class FibonacciConsoleRunner implements CommandLineRunner {

    @Autowired
    private FibonacciService fibonacciService;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Калькулятор чисел Фибоначчи ===");
        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1 - Вычисление члена последовательности");
            System.out.println("2 - Показать размер кэша");
            System.out.println("3 - Очистить кэш");
            System.out.println("0 - Выход");
            System.out.print("Ваш выбор: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    calculateFibonacci(scanner);
                    break;
                case "2":
                    showCacheInfo();
                    break;
                case "3":
                    fibonacciService.clearCache();
                    break;
                case "0":
                    System.out.println("До свидания!");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void calculateFibonacci(Scanner scanner) {
        System.out.print("Введите значение: ");
        try {
            int n = Integer.parseInt(scanner.nextLine().trim());
            long result = fibonacciService.calculate(n);

            System.out.println("\nРезультат:");
            System.out.println("F(" + n + ") = " + result);

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите целое число");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void showCacheInfo() {
        System.out.println("Количество элементов в кэше: " + fibonacciService.getCacheSize());
    }
}