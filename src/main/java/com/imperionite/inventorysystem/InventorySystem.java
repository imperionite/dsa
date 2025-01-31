package com.imperionite.inventorysystem;

import java.util.LinkedList;

// Stock class to store inventory details
class Stock {
    String dateEntered, stockLabel, brand, engineNumber, status;

    // Constructor to initialize stock details
    public Stock(String dateEntered, String stockLabel, String brand, String engineNumber, String status) {
        this.dateEntered = dateEntered;
        this.stockLabel = stockLabel;
        this.brand = brand;
        this.engineNumber = engineNumber;
        this.status = status;
    }

    // Override toString() for easy display of stock details
    @Override
    public String toString() {
        return brand + " - " + engineNumber + " (" + dateEntered + ") | Status: " + status;
    }
}

// Inventory Management using Java's LinkedList
class InventoryList {
    private LinkedList<Stock> inventory; // Built-in LinkedList

    // Constructor initializes the linked list
    public InventoryList() {
        inventory = new LinkedList<>();
    }

    // Method to add a new stock item
    public void addStock(String dateEntered, String stockLabel, String brand, String engineNumber, String status) {
        Stock newStock = new Stock(dateEntered, stockLabel, brand, engineNumber, status);
        inventory.addLast(newStock); // Append to the end of the list
        System.out.println("Stock added: " + newStock);
    }

    // Method to display all stock items
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

        // Adding stock items using built-in LinkedList
        inventory.addStock("2/1/2023", "Old", "Honda", "142QVTSIUR", "On-hand");
        inventory.addStock("3/7/2023", "New", "Kawasaki", "392XSUBMUW", "On-hand");

        // Displaying the inventory list
        inventory.displayStock();
    }
}
