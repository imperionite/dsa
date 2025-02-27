package com.imperionite.inventorysystem;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class InventorySystem {

    // HashMap to store the inventory with engineNumber as the key
    private Map<String, Stock> inventoryMap;
    private BST bst;

    // Constructor
    public InventorySystem() {
        inventoryMap = new HashMap<>();
        bst = new BST();
    }

    // Method to read CSV file and load into HashMap and BST
    public void loadInventoryFromCSV(String filePath) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy"); // Correct format (MM/dd/yyyy)
        
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {
                if (record[0].equals("Date Entered")) continue;  // Skip header row

                // Parse the date with the custom formatter
                LocalDate dateEntered = LocalDate.parse(record[0], formatter);
                String stockLabel = record[1];
                String brand = record[2];
                String engineNumber = record[3];
                String status = record[4];

                // Create a new Stock object
                Stock stock = new Stock(dateEntered, stockLabel, brand, engineNumber, status);

                // Insert into HashMap (engineNumber is the key)
                inventoryMap.put(engineNumber, stock);

                // Insert into BST for sorted inventory by brand
                bst.insert(stock);
            }

            // Debugging: Print out all the items in inventoryMap to verify it's populated
            System.out.println("Inventory loaded successfully. Total items: " + inventoryMap.size());

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    // Method to search inventory by engine number
    public Stock searchByEngineNumber(String engineNumber) {
        return inventoryMap.get(engineNumber);  // HashMap provides O(1) lookup time
    }

    // Method to search inventory by brand
    public void searchByBrand(String brand) {
        List<Stock> filteredStocks = bst.getFilteredStocksByBrand(brand);
        if (filteredStocks.isEmpty()) {
            System.out.println("No stocks found with brand: " + brand);
        } else {
            System.out.println("Stocks found with brand " + brand + ":");
            for (Stock stock : filteredStocks) {
                System.out.println(stock);
            }
        }
    }

    // Method for user interaction (recursive search)
    public void startUserSearch() {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Search by engine number");
            System.out.println("2. Search by brand");
            System.out.println("3. Exit");

            System.out.print("Enter your choice (1, 2, or 3): ");
            userInput = scanner.nextLine().trim();

            switch (userInput) {
                case "1":
                    // Search by engine number
                    System.out.print("Enter engine number to search: ");
                    String engineNumber = scanner.nextLine().trim();
                    Stock foundStock = searchByEngineNumber(engineNumber);
                    if (foundStock != null) {
                        System.out.println("Stock found: " + foundStock);
                    } else {
                        System.out.println("No stock found with engine number: " + engineNumber);
                    }
                    break;

                case "2":
                    // Search by brand
                    System.out.print("Enter brand to search: ");
                    String brand = scanner.nextLine().trim();
                    searchByBrand(brand);
                    break;

                case "3":
                    // Exit
                    System.out.println("Exiting the program.");
                    scanner.close();  // Close the scanner
                    return;  // Exit the loop and terminate the program

                default:
                    System.out.println("Invalid input. Please enter 1, 2, or 3.");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        InventorySystem inventorySystem = new InventorySystem();

        // Load inventory from CSV file
        String filePath = "data/inventory.csv";
        inventorySystem.loadInventoryFromCSV(filePath);

        // Start interactive search loop
        inventorySystem.startUserSearch();
    }
}
