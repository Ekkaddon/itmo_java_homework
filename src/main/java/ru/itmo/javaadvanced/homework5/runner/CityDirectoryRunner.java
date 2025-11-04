package ru.itmo.javaadvanced.homework5.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.itmo.javaadvanced.homework5.ui.ConsoleUI;

@Component
public class CityDirectoryRunner implements CommandLineRunner {

    private final ConsoleUI consoleUI;

    public CityDirectoryRunner(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    @Override
    public void run(String... args) {
        consoleUI.start();
    }
}