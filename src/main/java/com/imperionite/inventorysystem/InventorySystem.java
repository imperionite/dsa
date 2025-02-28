package com.imperionite.inventorysystem;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class InventorySystem {

    List<Stock> stockList = new ArrayList<>();
    // private Set<String> engineNumbers = new HashSet<>(); // To ensure unique
    // engine numbers
    static String CSV_FILE_PATH = "data/inventory.csv"; // Path to the CSV file
        StockBST stockBST = new StockBST(); // We now use the BST for stock operations
    
        public static void main(String[] args) {
            InventorySystem system = new InventorySystem();
            system.loadExistingStocks(); // Load existing stocks from the CSV file
    
            system.showMenu(); // Show menu for the user to choose an action
        }
    
        // Show the menu with options to add or delete stock
        public void showMenu() {
            Scanner scanner = new Scanner(System.in);
    
            System.out.println("\nWelcome to the Inventory System!");
            System.out.println("Choose an option:");
            System.out.println("1. Add New Stock");
            System.out.println("2. Delete Stock");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1/2/3): ");
    
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character left by nextInt()
    
            switch (choice) {
                case 1:
                    acceptUserInput(scanner); // Add new stock
                    break;
                case 2:
                    deleteStock(scanner); // Delete a stock
                    break;
                case 3:
                    System.out.println("Exiting the system...");
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice! Please try again.");
                    showMenu(); // Recursively show the menu if the user input is invalid
            }
    
            scanner.close(); // Close the scanner
        }
    
        // Method to accept user input for adding a new stock
        public void acceptUserInput(Scanner scanner) {
            try {
                System.out.println("Enter the stock details below:");
    
                // Prompt user for stock details
                System.out.print("Enter Date (MM/DD/YYYY): ");
                String dateEnteredInput = scanner.nextLine();
    
                // Parse the date input as LocalDate
                LocalDate dateEntered = parseDate(dateEnteredInput);
                if (dateEntered == null) {
                    System.out.println("Invalid date format. Please enter the date in MM/DD/YYYY format.");
                    return;
                }
    
                System.out.print("Enter Stock Label: ");
                String stockLabel = scanner.nextLine();
    
                System.out.print("Enter Brand: ");
                String brand = scanner.nextLine();
    
                System.out.print("Enter Engine Number: ");
                String engineNumber = scanner.nextLine();
    
                System.out.print("Enter Status (Available/Sold): ");
                String status = scanner.nextLine();
    
                // Add the stock using the provided details (passing LocalDate as the
                // dateEntered parameter)
                addNewStock(dateEntered, stockLabel, brand, engineNumber, status);
    
                // Ask if the user wants to add another stock
                System.out.print("Do you want to add another stock? (yes/no): ");
                String userChoice = scanner.nextLine();
    
                // If the user chooses "yes", recursively call the acceptUserInput method again
                if ("yes".equalsIgnoreCase(userChoice)) {
                    acceptUserInput(scanner); // Recursive call to allow for the next entry
                } else {
                    System.out.println("Stock addition complete.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    
        // Method to load existing stock data from the CSV file
        public void loadExistingStocks() {
            try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
                try (@SuppressWarnings("deprecation")
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {
                    for (CSVRecord record : csvParser) {
                        String dateEnteredString = record.get("Date Entered");
                        String stockLabel = record.get("Stock Label");
                        String brand = record.get("Brand");
                        String engineNumber = record.get("Engine Number");
                        String status = record.get("Status");
    
                        LocalDate parsedDate = parseDate(dateEnteredString);
                        if (parsedDate != null) {
                            Stock stock = new Stock(parsedDate, stockLabel, brand, engineNumber, status);
                            stockList.add(stock); // Add to stock list
                            stockBST.insert(stock); // Insert into BST
                        } else {
                            System.out.println("Error: Invalid date format for engine number " + engineNumber);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading existing stocks: " + e.getMessage());
            }
        }
    
        // Method to handle adding a new stock entry
        public boolean addNewStock(LocalDate dateEntered, String stockLabel, String brand, String engineNumber,
                String status) {
            // Check if the engine number already exists in the CSV
            if (engineNumberExists(engineNumber)) {
                System.out.println("Error: Duplicate engine number, stock cannot be added.");
                return false; // Engine number already exists, return false
            }
    
            // Create a new stock entry
            Stock newStock = new Stock(dateEntered, stockLabel, brand, engineNumber, status);
    
            // Add to stockList and StockBST
            stockList.add(newStock);
            stockBST.insert(newStock);
    
            // Rebuild the StockBST (this can be an optional step if you want to ensure the
            // tree is always updated)
            rebuildTreeFromList();
    
            // Save the stock to CSV
            saveStockToCSV(newStock);
    
            System.out.println("New stock added: " + newStock);
            return true; // Stock added successfully
        }
    
        // Rebuild the entire BST from the stockList
        private void rebuildTreeFromList() {
            // Empty the tree and then reinsert all items from stockList
            stockBST = new StockBST(); // Assuming stockBST is the reference to your tree
            for (Stock stock : stockList) {
                stockBST.insert(stock);
            }
        }
    
        // Method to check if engine number already exists
        public boolean engineNumberExists(String engineNumber) {
            // Check if engine number exists in BST
            Stock stock = stockBST.searchByEngineNumber(engineNumber);
            return stock != null;
        }
    
        // Method to delete a stock
        public void deleteStock(Scanner scanner) {
            System.out.print("Enter the Engine Number of the stock you want to delete: ");
            String engineNumberToDelete = scanner.nextLine();
    
            // Attempt to delete the stock with the given engine number
            boolean success = deleteStockByEngineNumber(engineNumberToDelete);
    
            if (success) {
                System.out.println("Stock with engine number " + engineNumberToDelete + " has been deleted.");
            } else {
                System.out.println("Stock with engine number " + engineNumberToDelete + " not found.");
            }
    
            // After deletion, ask if the user wants to delete another stock
            System.out.print("Do you want to delete another stock? (yes/no): ");
            String userChoice = scanner.nextLine();
            if ("yes".equalsIgnoreCase(userChoice)) {
                deleteStock(scanner); // Recursively allow deletion of another stock
            } else {
                System.out.println("Deletion process complete.");
            }
        }
    
        // Method to delete a stock by engine number using the BST
        public boolean deleteStockByEngineNumber(String engineNumber) {
            // First, search for the stock in the BST
            Stock stockToDelete = stockBST.searchByEngineNumber(engineNumber);
    
            if (stockToDelete != null) {
                // If the stock exists in the BST, delete it
                boolean deletedFromBST = stockBST.delete(engineNumber);
                if (deletedFromBST) {
                    System.out.println("Stock successfully deleted from BST.");
                } else {
                    System.out.println("Failed to delete stock from BST.");
                }
    
                // Now, delete the stock from the stockList
                boolean deletedFromList = stockList.removeIf(stock -> stock.getEngineNumber().equals(engineNumber));
                if (deletedFromList) {
                    System.out.println("Stock successfully deleted from stockList.");
                } else {
                    System.out.println("Failed to delete stock from stockList.");
                }
    
                // Rebuild the tree after deletion
                rebuildTreeFromList();
    
                return deletedFromBST && deletedFromList;
            } else {
                System.out.println("Stock with Engine Number " + engineNumber + " not found in BST.");
            }
            return false;
        }
    
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
                        csvPrinter.flush();
                    }
                } else {
                    // Append stock data without header
                    try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
                        csvPrinter.printRecord(stock.getDateEntered(), stock.getStockLabel(), stock.getBrand(),
                                stock.getEngineNumber(), stock.getStatus());
                        csvPrinter.flush();
                    }
                }
            } catch (IOException e) {
                System.out.println("Error saving stock to CSV: " + e.getMessage());
            }
        }
    
        // Method to parse date from the user input to LocalDate
        public LocalDate parseDate(String dateString) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e1) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    return LocalDate.parse(dateString, formatter);
                } catch (DateTimeParseException e2) {
                    return null; // Invalid date
                }
            }
        }
    
        // Method to show all stocks
        public void showAllStocks() {
            System.out.println("Current Inventory:");
            for (Stock stock : stockList) {
                System.out.println(stock);
            }
        }
    
        public void setCsvFilePath(String csvFilePath) {
            CSV_FILE_PATH = csvFilePath;
    }

}
