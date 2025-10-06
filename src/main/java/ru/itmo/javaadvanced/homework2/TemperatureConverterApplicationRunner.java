package ru.itmo.javaadvanced.homework2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.itmo.javaadvanced.homework2")
public class TemperatureConverterApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(TemperatureConverterApplicationRunner.class, args);
    }
}