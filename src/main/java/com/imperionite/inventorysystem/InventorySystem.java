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

    // Constructor
    public InventorySystem() {
        inventoryMap = new HashMap<>();
    }

    // Method to read CSV file and load into HashMap
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

    // Method for user interaction (recursive search)
    public void startUserSearch() {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            System.out.print("Enter engine number to search (or type 'exit' to quit): ");
            userInput = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(userInput)) {
                System.out.println("Exiting the program.");
                break;  // Exit the loop and terminate the program
            }

            // Search and display the result
            Stock foundStock = searchByEngineNumber(userInput);
            if (foundStock != null) {
                System.out.println("Stock found: " + foundStock);
            } else {
                System.out.println("No stock found with engine number: " + userInput);
            }
        }

        scanner.close();  // Close the scanner
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
