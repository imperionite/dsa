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
        // If tree is empty, return a new node
        if (root == null) {
            root = new Node(stock);
            return root;
        }

        // Otherwise, recur down the tree
        int comparison = stock.getBrand().compareToIgnoreCase(root.stock.getBrand());

        // Insert into the left or right subtree based on the comparison
        if (comparison < 0) {
            root.left = insertRec(root.left, stock);
        } else if (comparison > 0) {
            root.right = insertRec(root.right, stock);
        } else {
            // In case of equal brand, we can choose to insert to either left or right
            // For this example, we will insert on the right side
            root.right = insertRec(root.right, stock);
        }

        // Return the (unchanged) node pointer
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
