package com.imperionite.inventorysystem;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class InventorySystem {

    private static final String CSV_FILE_PATH = "data/inventory.csv"; // Path to the CSV file
    private List<Stock> stockList = new ArrayList<>();
    private Set<String> engineNumbersSet = new HashSet<>();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        InventorySystem system = new InventorySystem();

        // Load data from CSV
        system.loadStockData(CSV_FILE_PATH);

        // Start the deletion process
        system.startDeletionProcess();

        // Save the updated data back to CSV after deletion
        system.saveStockData(CSV_FILE_PATH);
    }

    // Load stock data from CSV
    public void loadStockData(String filename) {
        try (Reader reader = new FileReader(filename); @SuppressWarnings("deprecation")
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {
            Iterable<CSVRecord> records = csvParser.getRecords();

            for (CSVRecord record : records) {
                // Parse and map data
                LocalDate dateEntered = LocalDate.parse(record.get("Date Entered"), DateTimeFormatter.ofPattern("M/d/yyyy"));
                String stockLabel = record.get("Stock Label");
                String brand = record.get("Brand");
                String engineNumber = record.get("Engine Number");
                String status = record.get("Status");

                // Create stock object and add it to the list and hash set for engine numbers
                Stock stock = new Stock(dateEntered, stockLabel, brand, engineNumber, status);
                stockList.add(stock);
                engineNumbersSet.add(engineNumber);  // Add to HashSet for efficient search
            }

            System.out.println("Data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading CSV file: " + e.getMessage());
        }
    }

    // Perform deletion of stock by engine number
    public void deleteStockByEngineNumber(String engineNumber) {
        if (!engineNumbersSet.contains(engineNumber)) {
            System.out.println("Error: Stock with engine number " + engineNumber + " not found.");
            return;
        }

        // Find and remove the stock from the list
        Iterator<Stock> iterator = stockList.iterator();
        while (iterator.hasNext()) {
            Stock stock = iterator.next();
            if (stock.getEngineNumber().equals(engineNumber)) {
                iterator.remove();  // Remove the stock from the list
                engineNumbersSet.remove(engineNumber);  // Remove from HashSet
                System.out.println("Stock with engine number " + engineNumber + " has been deleted.");
                return;
            }
        }
    }

    // Start the deletion process: Prompt the user to delete by engine number or exit
    public void startDeletionProcess() {
        while (true) {
            System.out.print("Enter engine number to delete or type 'exit' to quit: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the application.");
                break;
            }

            deleteStockByEngineNumber(input);
        }
    }

    // Save the stock data back to CSV after deletion
    public void saveStockData(String filename) {
        try (Writer writer = new FileWriter(filename); @SuppressWarnings("deprecation")
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Date Entered", "Stock Label", "Brand", "Engine Number", "Status"))) {
            // Write each stock object as a CSV line
            for (Stock stock : stockList) {
                csvPrinter.printRecord(stock.getDateEntered().format(DateTimeFormatter.ofPattern("M/d/yyyy")),
                        stock.getStockLabel(),
                        stock.getBrand(),
                        stock.getEngineNumber(),
                        stock.getStatus());
            }

            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving CSV file: " + e.getMessage());
        }
    }

}