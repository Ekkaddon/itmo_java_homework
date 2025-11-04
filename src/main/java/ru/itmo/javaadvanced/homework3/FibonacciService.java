package ru.itmo.javaadvanced.homework3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для вычисления n-го члена последовательности Фибоначчи
 * Реализован как Singleton bean с возможностью кэширования
 */
@Service
public class FibonacciService {

    /**
     * Кэш для хранения вычисленных значений
     */
    private final Map<Integer, Long> cache = new HashMap<>();

    /**
     * Вычисляет n-ый член последовательности Фибоначчи
     *
     * @param n позиция в последовательности (начиная с 0)
     * @return значение n-го члена последовательности Фибоначчи
     */
    public long calculate(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n должно быть >= 0");
        }

        if (cache.containsKey(n)) {
            System.out.println("✓ Значение получено из кэша");
            return cache.get(n);
        }

        System.out.println("⚙ Вычисление нового значения...");

        if (n == 0) {
            return cacheAndReturn(0, 0L);
        }
        if (n == 1) {
            return cacheAndReturn(1, 1L);
        }

        long prev2 = 0L;
        long prev1 = 1L;
        long result = 0L;

        cache.put(0, prev2);
        cache.put(1, prev1);

        for (int i = 2; i <= n; i++) {
            result = prev1 + prev2;

            cache.put(i, result);

            prev2 = prev1;
            prev1 = result;
        }

        return result;
    }

    /**
     * Сохраняет значение в кэш и возвращает его
     */
    private long cacheAndReturn(int n, long value) {
        cache.put(n, value);

        return value;
    }

    /**
     * Очищает кэш
     */
    public void clearCache() {
        cache.clear();
        System.out.println("Кэш очищен");
    }

    /**
     * Возвращает размер кэша
     */
    public int getCacheSize() {
        return cache.size();
    }
}