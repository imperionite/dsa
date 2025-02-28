package com.imperionite.inventorysystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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
    void testDeleteStockByEngineNumber() {
        // Test deleting a stock by engine number
        LocalDate date = LocalDate.of(2023, 1, 1);
        inventorySystem.addNewStock(date, "New", "Honda", "12345", "On-hand");
        inventorySystem.addNewStock(date, "Old", "Yamaha", "67890", "Sold");

        // Delete an existing stock
        boolean deleted = inventorySystem.deleteStockByEngineNumber("12345");
        assertTrue(deleted);
        assertEquals(1, inventorySystem.stockList.size());
        assertNull(inventorySystem.stockBST.searchByEngineNumber("12345"));

        // Delete a non-existent stock
        boolean notDeleted = inventorySystem.deleteStockByEngineNumber("99999");
        assertFalse(notDeleted);
        assertEquals(1, inventorySystem.stockList.size()); // Size should remain the same
    }

    // Add this method to InventorySystem to set the CSV file path for testing
    public void setCsvFilePath(String csvFilePath) {
        InventorySystem.CSV_FILE_PATH = csvFilePath;
    }
}