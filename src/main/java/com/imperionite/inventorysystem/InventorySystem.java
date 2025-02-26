package com.imperionite.inventorysystem;

import org.apache.commons.csv.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class InventorySystem {

    private List<Stock> stockList = new ArrayList<>();
    private Set<String> engineNumbers = new HashSet<>(); // To ensure unique engine numbers
    private static final String CSV_FILE_PATH = "data/inventory.csv"; // Path to the CSV file

    public static void main(String[] args) {
        InventorySystem system = new InventorySystem();
        system.loadExistingStocks(); // Load existing stocks from the CSV file

        system.showAllStocks(); // Show all stocks before proceeding to user input
        system.acceptUserInput(); // Start the user input loop for adding new stock
    }

    // Method to accept user input for a new stock entry recursively
    public void acceptUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the stock details below:");

        // Prompt user for stock details
        System.out.print("Enter Date (MM/DD/YYYY): ");
        String dateEntered = scanner.nextLine();

        System.out.print("Enter Stock Label: ");
        String stockLabel = scanner.nextLine();

        System.out.print("Enter Brand: ");
        String brand = scanner.nextLine();

        System.out.print("Enter Engine Number: ");
        String engineNumber = scanner.nextLine();

        System.out.print("Enter Status: ");
        String status = scanner.nextLine();

        // Add the stock using the provided details
        addNewStock(dateEntered, stockLabel, brand, engineNumber, status);

        // Ask if the user wants to add another stock
        System.out.print("Do you want to add another stock? (yes/no): ");
        String userChoice = scanner.nextLine();

        // If the user chooses "yes", recursively call the acceptUserInput method again
        if ("yes".equalsIgnoreCase(userChoice)) {
            acceptUserInput(); // Recursive call to allow for the next entry
        } else {
            System.out.println("Stock addition complete.");
        }

        scanner.close(); // Close the scanner
    }

    // Method to handle adding a new stock entry
    public boolean addNewStock(String dateEntered, String stockLabel, String brand, String engineNumber,
            String status) {
        // Check if the engine number already exists in the CSV
        if (engineNumberExists(engineNumber)) {
            System.out.println("Error: Duplicate engine number, stock cannot be added.");
            return false; // Engine number already exists, return false
        }

        // If engine number is unique, proceed with adding the stock
        LocalDate parsedDate = parseDate(dateEntered);
        if (parsedDate == null) {
            System.out.println("Invalid date format: " + dateEntered);
            return false; // Invalid date, return false
        }

        // Add engine number to the set to ensure uniqueness
        engineNumbers.add(engineNumber);

        // Create a new stock entry and save it to CSV
        Stock newStock = new Stock(dateEntered, stockLabel, brand, engineNumber, status);
        stockList.add(newStock);
        saveStockToCSV(newStock); // Save the stock to CSV
        System.out.println("New stock added: " + newStock);
        return true; // Stock added successfully
    }

    // Method to check if engine number already exists in the CSV file
    public boolean engineNumberExists(String engineNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            try (@SuppressWarnings("deprecation")
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
                for (CSVRecord record : csvParser) {
                    String existingEngineNumber = record.get("Engine Number");
                    if (existingEngineNumber.equals(engineNumber)) {
                        return true; // Engine number found, return true
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking engine number existence: " + e.getMessage());
        }
        return false; // Engine number does not exist
    }

    // Method to parse the date with multiple formats
    public LocalDate parseDate(String dateEntered) {
        DateTimeFormatter[] dateFormats = {
                DateTimeFormatter.ofPattern("M/d/yyyy"), // For formats like 2/2/2024
                DateTimeFormatter.ofPattern("MM/dd/yyyy") // For formats like 02/02/2024
        };

        for (DateTimeFormatter formatter : dateFormats) {
            try {
                return LocalDate.parse(dateEntered, formatter);
            } catch (DateTimeParseException e) {
                continue; // Try the next format if parsing fails
            }
        }

        return null; // Return null if no valid date format is found
    }

    // Method to load existing stock data from the CSV file
    public void loadExistingStocks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            try (@SuppressWarnings("deprecation")
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {
                for (CSVRecord record : csvParser) {
                    String dateEntered = record.get("Date Entered");
                    String stockLabel = record.get("Stock Label");
                    String brand = record.get("Brand");
                    String engineNumber = record.get("Engine Number");
                    String status = record.get("Status");

                    // Parse the date
                    LocalDate parsedDate = parseDate(dateEntered);
                    if (parsedDate != null) {
                        // Add the stock to the inventory list
                        Stock stock = new Stock(dateEntered, stockLabel, brand, engineNumber, status);
                        stockList.add(stock);
                        engineNumbers.add(engineNumber); // Add engine number to the set
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading existing stocks: " + e.getMessage());
        }
    }

    // Method to save the new stock to the CSV file
    // Method to save the new stock to the CSV file
    public void saveStockToCSV(Stock stock) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            // Check if the file is empty to write header
            File file = new File(CSV_FILE_PATH);
            if (file.length() == 0) {
                try (@SuppressWarnings("deprecation")
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Date Entered",
                        "Stock Label", "Brand", "Engine Number", "Status"))) {
                    // Write the first stock record
                    csvPrinter.printRecord(stock.getDateEntered(), stock.getStockLabel(), stock.getBrand(),
                            stock.getEngineNumber(), stock.getStatus());
                    csvPrinter.flush(); // Ensure it's written and on a new line
                }
            } else {
                // Append stock data without header, this ensures new record is in the next line
                try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
                    // Write the stock data, ensuring no extra newlines are added
                    csvPrinter.printRecord(stock.getDateEntered(), stock.getStockLabel(), stock.getBrand(),
                            stock.getEngineNumber(), stock.getStatus());
                    csvPrinter.flush(); // Ensure it's written immediately
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving stock to CSV: " + e.getMessage());
        }
    }

    // Show all stocks in the inventory
    public void showAllStocks() {
        System.out.println("Current Inventory:");
        for (Stock stock : stockList) {
            System.out.println(stock);
        }
    }
}