package com.imperionite.inventorysystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InventorySystemTest {

    private InventorySystem inventorySystem;
    @TempDir
    Path tempDir;
    private Path tempFilePath;

    @BeforeEach
    void setUp() throws IOException {
        inventorySystem = new InventorySystem();
        tempFilePath = tempDir.resolve("test_inventory.csv");
        inventorySystem.setCsvFilePath(tempFilePath.toString()); // Set the temporary file path
        Files.createFile(tempFilePath); // Create the file.

        // Clear the stockList and stockBST before each test
        inventorySystem.stockList.clear();
        inventorySystem.stockBST = new StockBST();
    }

    @Test
    void testAddNewStock() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        boolean added = inventorySystem.addNewStock(date, "New", "Honda", "12345", "On-hand");
        assertTrue(added);
        assertEquals(1, inventorySystem.stockList.size());
        assertEquals(1, inventorySystem.stockBST.inorderTraversal().size());
    }

    @Test
    void testAddDuplicateEngineNumber() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        inventorySystem.addNewStock(date, "New", "Honda", "12345", "On-hand");
        boolean added = inventorySystem.addNewStock(date, "Old", "Yamaha", "12345", "Sold");
        assertFalse(added);
        assertEquals(1, inventorySystem.stockList.size());
    }

    @Test
    void testLoadExistingStocks() throws IOException {
        // Create a test CSV file with data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath.toString()))) {
            writer.write("Date Entered,Stock Label,Brand,Engine Number,Status\n");
            writer.write("01/01/2023,New,Honda,12345,On-hand\n");
            writer.write("02/02/2023,Old,Yamaha,67890,Sold\n");
        }

        inventorySystem.loadExistingStocks();
        assertEquals(2, inventorySystem.stockList.size());
        assertEquals(2, inventorySystem.stockBST.inorderTraversal().size());
    }

    @Test
    void testSaveStockToCSV() throws IOException {
        LocalDate date = LocalDate.of(2023, 1, 1);
        Stock stock = new Stock(date, "New", "Honda", "12345", "On-hand");
        inventorySystem.saveStockToCSV(stock);

        String fileContent = Files.readString(tempFilePath);
        assertTrue(fileContent.contains("12345"));
    }

    @Test
    void testParseDateValid() {
        LocalDate date = inventorySystem.parseDate("01/01/2023");
        assertEquals(LocalDate.of(2023, 1, 1), date);
    }

    @Test
    void testParseDateInvalid() {
        LocalDate date = inventorySystem.parseDate("invalid_date");
        assertNull(date);
    }

    @Test
    void testShowAllStocks() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        inventorySystem.addNewStock(date, "New", "Honda", "12345", "On-hand");
        inventorySystem.addNewStock(date, "Old", "Yamaha", "67890", "Sold");

        // Capture System.out to verify the output
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));
        inventorySystem.showAllStocks();
        String consoleOutput = out.toString();
        assertTrue(consoleOutput.contains("12345"));
        assertTrue(consoleOutput.contains("67890"));
    }
    @Test
    void testEngineNumberExists() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        inventorySystem.addNewStock(date, "New", "Honda", "12345", "On-hand");
        assertTrue(inventorySystem.engineNumberExists("12345"));
        assertFalse(inventorySystem.engineNumberExists("67890"));
    }

    // Add this method to InventorySystem to set the CSV file path for testing
    public void setCsvFilePath(String csvFilePath) {
        InventorySystem.CSV_FILE_PATH = csvFilePath;
    }
}