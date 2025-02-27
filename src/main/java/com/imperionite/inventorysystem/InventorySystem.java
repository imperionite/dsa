package com.imperionite.inventorysystem;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class InventorySystem {

    public static void main(String[] args) {
        // Create an instance of BST
        BST bst = new BST();

        // Read the CSV file and insert data into the BST
        try (CSVReader csvReader = new CSVReader(new FileReader("data/inventory.csv"))) {
            List<String[]> records = csvReader.readAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

            // Skip the header and process each record
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                String dateEnteredStr = record[0];
                String stockLabel = record[1];
                String brand = record[2];
                String engineNumber = record[3];
                String status = record[4];

                // Convert date string to LocalDate
                LocalDate dateEntered = LocalDate.parse(dateEnteredStr, formatter);

                // Create a Stock object and insert it into the BST
                Stock stock = new Stock(dateEntered, stockLabel, brand, engineNumber, status);
                bst.insert(stock);
            }

            // Ask the user for brand filtering (optional)
            Scanner scanner = new Scanner(System.in);
            System.out.println("Do you want to filter by a specific brand? (yes/no)");
            String filterChoice = scanner.nextLine().trim().toLowerCase();

            List<Stock> sortedStocks;

            if (filterChoice.equals("yes")) {
                System.out.println("Enter the brand name to filter by (e.g., Honda, Kawasaki):");
                String brandToFilter = scanner.nextLine().trim();
                sortedStocks = bst.getFilteredStocksByBrand(brandToFilter); // Get filtered stocks
            } else {
                sortedStocks = bst.getSortedStocks();  // Get all stocks sorted by brand
            }

            // Display the sorted or filtered stocks
            System.out.println("Inventory sorted by Brand (filtered by requested brand if any):");
            if (sortedStocks.isEmpty()) {
                System.out.println("No records found for the selected brand.");
            } else {
                for (Stock stock : sortedStocks) {
                    System.out.println(stock);
                }
            }

            scanner.close();

        } catch (IOException | CsvException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
