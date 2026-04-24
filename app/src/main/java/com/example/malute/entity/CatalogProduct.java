package com.example.malute.entity;

public class CatalogProduct {
    private String id;
    private String title;
    private String category;
    private String typeCloses;
    private String priceText;
    private double priceValue;
    private String description;
    private String consumption;

    public CatalogProduct(String id, String title, String category, String typeCloses, String priceText, double priceValue, String description, String consumption) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.typeCloses = typeCloses;
        this.priceText = priceText;
        this.priceValue = priceValue;
        this.description = description;
        this.consumption = consumption;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getTypeCloses() { return typeCloses; }
    public String getPriceText() { return priceText; }
    public double getPriceValue() { return priceValue; }
    public String getDescription() { return description; }
    public String getConsumption() { return consumption; }
}