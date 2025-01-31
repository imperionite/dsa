package com.imperionite.inventorysystem;

import java.util.Arrays;

// Stock class to store inventory details
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

// Inventory Management using Array + Merge Sort
class InventoryArray {
    private Stock[] inventory;
    private int size; // Track the number of stock items

    public InventoryArray(int capacity) {
        inventory = new Stock[capacity]; // Fixed-size array
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

    // Merge Sort Wrapper
    public void sortByBrand() {
        mergeSort(inventory, 0, size - 1);
    }

    // Merge Sort Algorithm
    private void mergeSort(Stock[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    // Merging sorted subarrays
    private void merge(Stock[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Stock[] leftArray = new Stock[n1];
        Stock[] rightArray = new Stock[n2];

        for (int i = 0; i < n1; i++) leftArray[i] = arr[left + i];
        for (int j = 0; j < n2; j++) rightArray[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i].brand.compareTo(rightArray[j].brand) <= 0) {
                arr[k++] = leftArray[i++];
            } else {
                arr[k++] = rightArray[j++];
            }
        }

        while (i < n1) arr[k++] = leftArray[i++];
        while (j < n2) arr[k++] = rightArray[j++];
    }

    // Display stock items
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
        InventoryArray inventory = new InventoryArray(5);

        inventory.addStock("03/10/2023", "New", "Kawasaki", "ENG12345", "On-hand");
        inventory.addStock("04/12/2023", "Old", "Honda", "ENG67890", "Sold");
        inventory.addStock("05/22/2023", "New", "Honda", "ENG54321", "On-hand");
        inventory.addStock("06/29/2023", "Old", "Suzuki", "ENG98765", "Sold");

        System.out.println("\nBefore Sorting:");
        inventory.displayStock();

        inventory.sortByBrand();

        System.out.println("\nAfter Sorting by Brand:");
        inventory.displayStock();
    }
}
