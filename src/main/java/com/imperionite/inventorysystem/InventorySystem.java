package com.imperionite.inventorysystem;

import java.util.ArrayList;
import java.util.List;

// Stock class
class Stock {
    String dateEntered, stockLabel, brand, engineNumber, status;

    public Stock(String dateEntered, String stockLabel, String brand, String engineNumber, String status) {
        this.dateEntered = dateEntered;
        this.stockLabel = stockLabel;
        this.brand = brand;
        this.engineNumber = engineNumber;
        this.status = status;
    }

    @Override
    public String toString() {
        return brand + " - " + engineNumber + " (" + dateEntered + ") | Status: " + status;
    }
}

// Inventory System with various search methods
class InventoryArray {
    private Stock[] inventory;
    private int size; // Tracks the number of stock items

    public InventoryArray(int capacity) {
        inventory = new Stock[capacity];
        size = 0;
    }

    // Add stock item
    public void addStock(String dateEntered, String stockLabel, String brand, String engineNumber, String status) {
        if (size == inventory.length) {
            System.out.println("Inventory is full! Cannot add more stock.");
            return;
        }
        inventory[size++] = new Stock(dateEntered, stockLabel, brand, engineNumber, status);
    }

    // Merge Sort for sorting by Engine Number (needed for Binary Search)
    public void sortByEngineNumber() {
        mergeSort(inventory, 0, size - 1);
    }

    private void mergeSort(Stock[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    private void merge(Stock[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Stock[] leftArray = new Stock[n1];
        Stock[] rightArray = new Stock[n2];

        for (int i = 0; i < n1; i++) leftArray[i] = arr[left + i];
        for (int j = 0; j < n2; j++) rightArray[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i].engineNumber.compareTo(rightArray[j].engineNumber) <= 0) {
                arr[k++] = leftArray[i++];
            } else {
                arr[k++] = rightArray[j++];
            }
        }

        while (i < n1) arr[k++] = leftArray[i++];
        while (j < n2) arr[k++] = rightArray[j++];
    }

    // Binary Search for Engine Number (requires sorted array)
    public Stock binarySearchByEngine(String engineNumber) {
        int left = 0, right = size - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = inventory[mid].engineNumber.compareTo(engineNumber);

            if (comparison == 0) return inventory[mid]; // Found
            else if (comparison < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null; // Not found
    }

    // Linear Search for Brand (returns multiple results)
    public List<Stock> searchByBrand(String brand) {
        List<Stock> results = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (inventory[i].brand.equalsIgnoreCase(brand)) {
                results.add(inventory[i]);
            }
        }
        return results;
    }

    // Linear Search for Status (returns multiple results)
    public List<Stock> searchByStatus(String status) {
        List<Stock> results = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (inventory[i].status.equalsIgnoreCase(status)) {
                results.add(inventory[i]);
            }
        }
        return results;
    }

    // Linear Search for Date Entered
    public List<Stock> searchByDateEntered(String date) {
        List<Stock> results = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (inventory[i].dateEntered.equals(date)) {
                results.add(inventory[i]);
            }
        }
        return results;
    }

    // Display inventory
    public void displayStock() {
        if (size == 0) {
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("\nCurrent Inventory:");
        for (int i = 0; i < size; i++) {
            System.out.println(inventory[i]);
        }
    }
}

// Execution Example
public class InventorySystem {
    public static void main(String[] args) {
        InventoryArray inventory = new InventoryArray(6);

        // Adding stock items
        inventory.addStock("03/10/2023", "New", "Kawasaki", "ENG12345", "On-hand");
        inventory.addStock("04/12/2023", "Old", "Honda", "ENG67890", "Sold");
        inventory.addStock("05/22/2023", "New", "Honda", "ENG54321", "On-hand");
        inventory.addStock("06/29/2023", "Old", "Suzuki", "ENG98765", "Sold");


        // Sorting before binary search
        inventory.sortByEngineNumber();

        // 🔎 Searching for stock by Engine Number (Binary Search)
        String searchEngine = "ENG54321";
        Stock foundStock = inventory.binarySearchByEngine(searchEngine);
        System.out.println("\n🔍 Search by Engine Number: " + (foundStock != null ? foundStock : "Not Found"));

        // 🔎 Searching for stock by Brand (Linear Search)
        String searchBrand = "Suzuki";
        List<Stock> brandResults = inventory.searchByBrand(searchBrand);
        System.out.println("\n🔍 Search by Brand: " + (brandResults.isEmpty() ? "Not Found" : brandResults));

        // 🔎 Searching for stock by Status (Linear Search)
        String searchStatus = "Sold";
        List<Stock> statusResults = inventory.searchByStatus(searchStatus);
        System.out.println("\n🔍 Search by Status: " + (statusResults.isEmpty() ? "Not Found" : statusResults));

        // 🔎 Searching for stock by Date Entered (Linear Search)
        String searchDate = "04/12/2023";
        List<Stock> dateResults = inventory.searchByDateEntered(searchDate);
        System.out.println("\n🔍 Search by Date Entered: " + (dateResults.isEmpty() ? "Not Found" : dateResults));
    }
}
