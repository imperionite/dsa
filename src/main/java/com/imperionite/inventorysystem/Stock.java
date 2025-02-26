package com.imperionite.inventorysystem;

import java.time.LocalDate;

public class Stock {
    private String dateEntered;
    private LocalDate parsedDate;
    private String stockLabel;
    private String brand;
    private String engineNumber;
    private String status;

    // Constructor and getters for the fields
    public Stock(String dateEntered, LocalDate parsedDate, String stockLabel, String brand, String engineNumber,
            String status) {
        this.dateEntered = dateEntered;
        this.parsedDate = parsedDate;
        this.stockLabel = stockLabel;
        this.brand = brand;
        this.engineNumber = engineNumber;
        this.status = status;
    }

    public String getDateEntered() {
        return dateEntered;
    }

    public LocalDate getParsedDate() {
        return parsedDate;
    }

    public String getStockLabel() {
        return stockLabel;
    }

    public String getBrand() {
        return brand;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("Date: %s, Brand: %s, Engine Number: %s, Status: %s",
                dateEntered, brand, engineNumber, status);
    }
}
