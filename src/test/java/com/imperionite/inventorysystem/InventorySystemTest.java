package com.imperionite.inventorysystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventorySystemTest {

    private BST bst;

    @BeforeEach
    void setUp() {
        bst = new BST();
    }

    @Test
    void testInsertAndGetSortedStocks() {
        // Arrange
        Stock stock1 = new Stock(LocalDate.parse("2/28/2025", DateTimeFormatter.ofPattern("M/d/yyyy")), "Civic", "Honda", "1234", "Available");
        Stock stock2 = new Stock(LocalDate.parse("3/1/2025", DateTimeFormatter.ofPattern("M/d/yyyy")), "Accord", "Honda", "5678", "Sold");
        Stock stock3 = new Stock(LocalDate.parse("2/25/2025", DateTimeFormatter.ofPattern("M/d/yyyy")), "Ninja", "Kawasaki", "91011", "Available");

        // Act
        bst.insert(stock1);
        bst.insert(stock2);
        bst.insert(stock3);

        List<Stock> sortedStocks = bst.getSortedStocks();

        // Assert
        assertEquals(3, sortedStocks.size());
        assertEquals("Civic", sortedStocks.get(0).getStockLabel());
        assertEquals("Accord", sortedStocks.get(1).getStockLabel());
        assertEquals("Ninja", sortedStocks.get(2).getStockLabel());
    }

    @Test
    void testGetFilteredStocksByBrand() {
        // Arrange
        Stock stock1 = new Stock(LocalDate.parse("2/28/2025", DateTimeFormatter.ofPattern("M/d/yyyy")), "Civic", "Honda", "1234", "Available");
        Stock stock2 = new Stock(LocalDate.parse("3/1/2025", DateTimeFormatter.ofPattern("M/d/yyyy")), "Accord", "Honda", "5678", "Sold");
        Stock stock3 = new Stock(LocalDate.parse("2/25/2025", DateTimeFormatter.ofPattern("M/d/yyyy")), "Ninja", "Kawasaki", "91011", "Available");

        // Act
        bst.insert(stock1);
        bst.insert(stock2);
        bst.insert(stock3);

        List<Stock> filteredStocks = bst.getFilteredStocksByBrand("Honda");

        // Assert
        assertEquals(2, filteredStocks.size());
        assertTrue(filteredStocks.stream().allMatch(stock -> stock.getBrand().equalsIgnoreCase("Honda")));
    }

    @Test
    void testGetFilteredStocksByBrand_NoMatches() {
        // Arrange
        Stock stock1 = new Stock(LocalDate.parse("2/28/2025", DateTimeFormatter.ofPattern("M/d/yyyy")), "Civic", "Honda", "1234", "Available");
        
        // Act
        bst.insert(stock1);
        
        List<Stock> filteredStocks = bst.getFilteredStocksByBrand("Yamaha");

        // Assert
        assertTrue(filteredStocks.isEmpty());
    }
}

