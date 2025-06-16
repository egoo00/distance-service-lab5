package com.example.distanceservice.dto;

import java.util.List;

public class GeocodingResponse {
    private List<Result> results;

   
    public List<Result> getResults() {
        return results;
    }

   
    public void setResults(List<Result> results) {
        this.results = results;
    }
}
