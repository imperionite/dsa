package com.imperionite.inventorysystem;

// import java.time.LocalDate;

public class Stock {
    private String dateEntered;
    private String stockLabel;
    private String brand;
    private String engineNumber;
    private String status;

    public Stock(String dateEntered, String stockLabel, String brand, String engineNumber, String status) {
        this.dateEntered = dateEntered;
        this.stockLabel = stockLabel;
        this.brand = brand;
        this.engineNumber = engineNumber;
        this.status = status;
    }

    // Getter methods for the properties
    public String getDateEntered() {
        return dateEntered;
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
        return "Stock [Date Entered=" + dateEntered + ", Stock Label=" + stockLabel + ", Brand=" + brand
                + ", Engine Number=" + engineNumber + ", Status=" + status + "]";
    }
}
