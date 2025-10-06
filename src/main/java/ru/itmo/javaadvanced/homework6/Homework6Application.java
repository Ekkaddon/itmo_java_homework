package ru.itmo.javaadvanced.homework6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.itmo.javaadvanced.homework6")
public class Homework6Application {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "homework6");
        SpringApplication.run(Homework6Application.class, args);
    }
}