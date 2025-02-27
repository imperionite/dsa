package com.imperionite.inventorysystem;

public class StockBST {
    private StockNode root;

    public StockBST() {
        this.root = null;
    }

    // Insert a new stock into the BST
    public void insert(Stock stock) {
        root = insertRec(root, stock);
    }

    private StockNode insertRec(StockNode root, Stock stock) {
        if (root == null) {
            root = new StockNode(stock); // If the tree is empty, create a new node
            return root;
        }

        // Recursively find the correct position to insert based on engine number
        if (stock.getEngineNumber().compareTo(root.stock.getEngineNumber()) < 0) {
            root.left = insertRec(root.left, stock); // Insert in the left subtree
        } else if (stock.getEngineNumber().compareTo(root.stock.getEngineNumber()) > 0) {
            root.right = insertRec(root.right, stock); // Insert in the right subtree
        }

        return root;
    }

   
    // Inorder traversal to get the sorted stock list
    public void inorder() {
        inorderRec(root);
    }

    private void inorderRec(StockNode root) {
        if (root != null) {
            inorderRec(root.left); // Traverse left subtree
            System.out.println(root.stock); // Print stock details
            inorderRec(root.right); // Traverse right subtree
        }
    }

    // Search for a stock by engine number and return the stock if found
    public Stock searchByEngineNumber(String engineNumber) {
        return searchByEngineNumberRec(root, engineNumber);
    }

    private Stock searchByEngineNumberRec(StockNode root, String engineNumber) {
        if (root == null) {
            return null; // Stock not found
        }

        // Compare engine number
        int comparison = engineNumber.compareTo(root.stock.getEngineNumber());
        if (comparison == 0) {
            return root.stock; // Found the stock
        }

        // Recur for left or right subtree based on comparison
        if (comparison < 0) {
            return searchByEngineNumberRec(root.left, engineNumber);
        } else {
            return searchByEngineNumberRec(root.right, engineNumber);
        }
    }
}