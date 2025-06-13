package com.example.distanceservice.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SimpleCache {

    // Ключи в формате "cities_<countryName>" уникальны, что минимизирует коллизии.
    // String использует свои реализации equals() и hashCode(), обеспечивая корректное сравнение.
    private final Map<String, List<?>> cache = new HashMap<>();

    public void put(String key, List<?> value) {
        cache.put(key, value);
    }

    public List<?> get(String key) {
        return cache.get(key);
    }

    public void clear() {
        cache.clear();
    }
}