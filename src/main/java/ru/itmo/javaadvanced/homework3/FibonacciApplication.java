package ru.itmo.javaadvanced.homework3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главное приложение для вычисления чисел Фибоначчи
 */
@SpringBootApplication(scanBasePackages = "ru.itmo.javaadvanced.homework3")
public class FibonacciApplication {

    public static void main(String[] args) {
        SpringApplication.run(FibonacciApplication.class, args);
    }
}