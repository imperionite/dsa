package com.imperionite.inventorysystem;

import java.util.ArrayList;
import java.util.List;

public class BST {
    private Node root;

    private static class Node {
        Stock stock;
        Node left, right;

        public Node(Stock stock) {
            this.stock = stock;
            left = right = null;
        }
    }

    // Insert method
    public void insert(Stock stock) {
        root = insertRec(root, stock);
    }

    private Node insertRec(Node root, Stock stock) {
        if (root == null) {
            root = new Node(stock);
            return root;
        }

        int comparison = stock.getBrand().compareToIgnoreCase(root.stock.getBrand());

        if (comparison < 0) {
            root.left = insertRec(root.left, stock);
        } else if (comparison > 0) {
            root.right = insertRec(root.right, stock);
        } else {
            // Handle equal brands (e.g., insert based on engine number as a secondary sort)
            int engineComparison = stock.getEngineNumber().compareToIgnoreCase(root.stock.getEngineNumber());
            if (engineComparison < 0) {
                root.left = insertRec(root.left, stock);
            } else {
                root.right = insertRec(root.right, stock);
            }
        }

        return root;
    }

    // Inorder traversal to get the sorted inventory
    public List<Stock> getSortedStocks() {
        List<Stock> sortedStocks = new ArrayList<>();
        inorderTraversal(root, sortedStocks);
        return sortedStocks;
    }

    private void inorderTraversal(Node root, List<Stock> sortedStocks) {
        if (root != null) {
            inorderTraversal(root.left, sortedStocks);
            sortedStocks.add(root.stock); // Add the stock to the list
            inorderTraversal(root.right, sortedStocks);
        }
    }

    // Get stocks filtered by brand
    public List<Stock> getFilteredStocksByBrand(String brand) {
        List<Stock> filteredStocks = new ArrayList<>();
        filterByBrandRec(root, brand, filteredStocks);
        return filteredStocks;
    }

    private void filterByBrandRec(Node root, String brand, List<Stock> filteredStocks) {
        if (root != null) {
            // Check the current node's brand (case-insensitive)
            if (root.stock.getBrand().equalsIgnoreCase(brand)) {
                filteredStocks.add(root.stock);
            }
            // Recur on the left and right subtrees
            filterByBrandRec(root.left, brand, filteredStocks);
            filterByBrandRec(root.right, brand, filteredStocks);
        }
    }
}
