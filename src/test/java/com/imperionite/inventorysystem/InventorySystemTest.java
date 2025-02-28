package com.imperionite.inventorysystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StockBSTTest {

    private StockBST stockBST;

    @BeforeEach
    void setUp() {
        // Initialize a new StockBST before each test
        stockBST = new StockBST();
    }

    @Test
    void testInsertAndSearch() {
        // Test inserting stocks and searching for them by engine number
        Stock stock1 = new Stock(LocalDate.of(2023, 1, 1), "New", "Honda", "12345", "On-hand");
        Stock stock2 = new Stock(LocalDate.of(2023, 2, 1), "Old", "Yamaha", "67890", "Sold");
        Stock stock3 = new Stock(LocalDate.of(2023, 3, 1), "Used", "Suzuki", "34567", "On-hand");

        // Insert stocks into the BST
        stockBST.insert(stock1);
        stockBST.insert(stock2);
        stockBST.insert(stock3);

        // Search for stocks by engine number
        Stock foundStock1 = stockBST.searchByEngineNumber("12345");
        Stock foundStock2 = stockBST.searchByEngineNumber("67890");
        Stock foundStock3 = stockBST.searchByEngineNumber("34567");
        Stock notFoundStock = stockBST.searchByEngineNumber("99999");

        // Assert that the found stocks are correct and the non-existent stock is null
        assertEquals(stock1, foundStock1);
        assertEquals(stock2, foundStock2);
        assertEquals(stock3, foundStock3);
        assertNull(notFoundStock);
    }

    @Test
    void testInorderTraversal() {
        // Test the inorder traversal of the BST
        Stock stock1 = new Stock(LocalDate.of(2023, 1, 1), "New", "Honda", "12345", "On-hand");
        Stock stock2 = new Stock(LocalDate.of(2023, 2, 1), "Old", "Yamaha", "67890", "Sold");
        Stock stock3 = new Stock(LocalDate.of(2023, 3, 1), "Used", "Suzuki", "34567", "On-hand");

        // Insert stocks into the BST
        stockBST.insert(stock1);
        stockBST.insert(stock2);
        stockBST.insert(stock3);

        // Capture System.out to verify inorder output
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        // Perform inorder traversal
        stockBST.inorder();

        // Get the console output
        String consoleOutput = out.toString();

        // Assert that the output contains the engine numbers of the stocks
        assertTrue(consoleOutput.contains("12345"));
        assertTrue(consoleOutput.contains("34567"));
        assertTrue(consoleOutput.contains("67890"));

        // Reset System.out
        System.setOut(System.out);
    }

    @Test
    void testInsertWithDuplicateEngineNumbers() {
        // Test inserting stocks with duplicate engine numbers
        Stock stock1 = new Stock(LocalDate.of(2023, 1, 1), "New", "Honda", "12345", "On-hand");
        Stock stock2 = new Stock(LocalDate.of(2023, 2, 1), "Old", "Yamaha", "12345", "Sold");

        // Insert stocks into the BST
        stockBST.insert(stock1);
        stockBST.insert(stock2);

        // Search for the stock by engine number
        Stock foundStock = stockBST.searchByEngineNumber("12345");

        // Assert that only the first inserted stock is found
        assertEquals(stock1, foundStock);
    }
}