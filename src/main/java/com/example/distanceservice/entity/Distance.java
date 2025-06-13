package com.example.distanceservice.entity;

public class Distance {
    private String id; // Например, комбинация fromCity и toCity
    private String fromCity;
    private String toCity;
    private double distance;
    private String unit;

    // Конструкторы
    public Distance() {}
    public Distance(String id, String fromCity, String toCity, double distance, String unit) {
        this.id = id;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.distance = distance;
        this.unit = unit;
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFromCity() { return fromCity; }
    public void setFromCity(String fromCity) { this.fromCity = fromCity; }
    public String getToCity() { return toCity; }
    public void setToCity(String toCity) { this.toCity = toCity; }
    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}