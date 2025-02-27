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

    // Delete stock by engine number
    // Delete stock by engine number
    public boolean delete(String engineNumber) {
        root = deleteRec(root, engineNumber); // Perform deletion
        return root != null; // Return true if root is not null after deletion
    }

    private StockNode deleteRec(StockNode root, String engineNumber) {
        if (root == null) {
            System.out.println("Node with engine number " + engineNumber + " not found.");
            return null; // Stock not found
        }
    
        // Debugging: Print comparisons at each step
        System.out.println("Comparing with node engine number: " + root.stock.getEngineNumber());
    
        if (engineNumber.compareTo(root.stock.getEngineNumber()) < 0) {
            System.out.println("Going left, current node engine number is: " + root.stock.getEngineNumber());
            root.left = deleteRec(root.left, engineNumber);
        } else if (engineNumber.compareTo(root.stock.getEngineNumber()) > 0) {
            System.out.println("Going right, current node engine number is: " + root.stock.getEngineNumber());
            root.right = deleteRec(root.right, engineNumber);
        } else {
            System.out.println("Found node with engine number: " + engineNumber);
            // Case 1: No children (leaf node)
            if (root.left == null && root.right == null) {
                return null;
            }
            // Case 2: One child
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
    
            // Case 3: Two children, find the inorder successor (smallest in the right subtree)
            root.stock = minValueNode(root.right).stock; // Replace with inorder successor's stock
            root.right = deleteRec(root.right, root.stock.getEngineNumber()); // Delete the inorder successor
        }
    
        return root; // Return the updated root
    }
    

    // Find the node with the smallest value (inorder successor)
    private StockNode minValueNode(StockNode root) {
        while (root.left != null) {
            root = root.left; // Keep traversing left to find the smallest node
        }
        return root; // Return the smallest node
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
