package com.imperionite.inventorysystem;


import java.util.LinkedList;

class Stock {
    String dateEntered, stockLabel, brand, engineNumber, status;

    public Stock(String dateEntered, String stockLabel, String brand, String engineNumber, String status) {
        this.dateEntered = dateEntered;
        this.stockLabel = stockLabel;
        this.brand = brand;
        this.engineNumber = engineNumber;
        this.status = status;
    }

    @Override
    public String toString() {
        return brand + " - " + engineNumber + " (" + dateEntered + ") | Status: " + status;
    }
}

class InventoryList {
    private LinkedList<Stock> inventory;

    public InventoryList() {
        inventory = new LinkedList<>();
    }

    // Add stock item
    public void addStock(String dateEntered, String stockLabel, String brand, String engineNumber, String status) {
        Stock newStock = new Stock(dateEntered, stockLabel, brand, engineNumber, status);
        inventory.addLast(newStock);
        System.out.println("Stock added: " + newStock);
    }

    // Delete stock item by engine number
    public void deleteStock(String engineNumber) {
        boolean removed = inventory.removeIf(stock -> stock.engineNumber.equals(engineNumber));

        if (removed) {
            System.out.println("Stock item removed: " + engineNumber);
        } else {
            System.out.println("Stock item not found: " + engineNumber);
        }
    }

    // Display stock items
    public void displayStock() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        System.out.println("\nCurrent Inventory:");
        for (Stock stock : inventory) {
            System.out.println(stock);
        }
    }
}

// Execution Example
public class InventorySystem {
    public static void main(String[] args) {
        InventoryList inventory = new InventoryList();

        // Adding stock items
        inventory.addStock("2/1/2023", "Old", "Honda", "142QVTSIUR", "On-hand");
        inventory.addStock("3/7/2023", "New", "Kawasaki", "392XSUBMUW", "On-hand");
        inventory.addStock("2/1/2023", "Old", "Honda", "PZCT1S00XE", "Sold");

        // Display inventory before deletion
        inventory.displayStock();

        // Deleting a stock item by engine number
        inventory.deleteStock("392XSUBMUW");

        // Display inventory after deletion
        inventory.displayStock();
    }
}
