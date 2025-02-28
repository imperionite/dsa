package com.imperionite.inventorysystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventorySystemSearchTest {

    private InventorySystem inventorySystem;

    @BeforeEach
    void setUp() {
        inventorySystem = new InventorySystem();
        loadTestInventory();
    }

    private void loadTestInventory() {
        // Manually loading test data into the inventory system
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

        // Creating sample Stock objects
        Stock stock1 = new Stock(LocalDate.parse("2/28/2025", formatter), "Civic", "Honda", "ENG123", "Available");
        Stock stock2 = new Stock(LocalDate.parse("3/1/2025", formatter), "Accord", "Honda", "ENG456", "Sold");
        Stock stock3 = new Stock(LocalDate.parse("2/25/2025", formatter), "Ninja", "Kawasaki", "ENG789", "Available");

        // Manually adding stocks to the inventoryMap and BST
        inventorySystem.inventoryMap.put(stock1.getEngineNumber(), stock1);
        inventorySystem.inventoryMap.put(stock2.getEngineNumber(), stock2);
        inventorySystem.inventoryMap.put(stock3.getEngineNumber(), stock3);
        
        inventorySystem.bst.insert(stock1);
        inventorySystem.bst.insert(stock2);
        inventorySystem.bst.insert(stock3);
    }

    @Test
    void testSearchByEngineNumber_Found() {
        // Act
        Stock foundStock = inventorySystem.searchByEngineNumber("ENG123");

        // Assert
        assertNotNull(foundStock, "Stock should be found for engine number ENG123");
        assertEquals("Civic", foundStock.getStockLabel());
    }

    @Test
    void testSearchByEngineNumber_NotFound() {
        // Act
        Stock foundStock = inventorySystem.searchByEngineNumber("ENG999");

        // Assert
        assertNull(foundStock, "Stock should not be found for engine number ENG999");
    }

    @Test
    void testSearchByBrand_Found() {
        // Act
        List<Stock> filteredStocks = inventorySystem.bst.getFilteredStocksByBrand("Honda");

        // Assert
        assertEquals(2, filteredStocks.size(), "There should be 2 stocks found for brand Honda");
        
        // Check if both stocks are from Honda
        assertTrue(filteredStocks.stream().allMatch(stock -> stock.getBrand().equalsIgnoreCase("Honda")));
    }

    @Test
    void testSearchByBrand_NotFound() {
        // Act
        List<Stock> filteredStocks = inventorySystem.bst.getFilteredStocksByBrand("Yamaha");

        // Assert
        assertTrue(filteredStocks.isEmpty(), "No stocks should be found for brand Yamaha");
    }
}
