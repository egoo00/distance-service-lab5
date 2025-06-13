package com.example.distanceservice.dto;

import java.util.List;

public class GeocodingResponse {
    private List<Result> results;

    // Добавленный геттер
    public List<Result> getResults() {
        return results;
    }

    // Сеттер (опционально)
    public void setResults(List<Result> results) {
        this.results = results;
    }
}