package com.example.apitransactions.model;

public class Transaction {
    private String id;
    private String date;
    private double amount;
    private String description;
    private String category;

    public String getTitle() {
        return description;
    }
    public String getId() { return id; }
    public String getDate() { return date; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
}
