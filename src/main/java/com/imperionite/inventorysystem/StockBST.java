package com.imperionite.inventorysystem;

import java.util.ArrayList;
import java.util.List;

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
            root = new StockNode(stock);
            return root;
        }

        if (stock.getEngineNumber().compareTo(root.stock.getEngineNumber()) < 0) {
            root.left = insertRec(root.left, stock);
        } else if (stock.getEngineNumber().compareTo(root.stock.getEngineNumber()) > 0) {
            root.right = insertRec(root.right, stock);
        }

        return root;
    }

    // Delete stock by engine number
    public boolean delete(String engineNumber) {
        root = deleteRec(root, engineNumber);
        return root != null;
    }

    private StockNode deleteRec(StockNode root, String engineNumber) {
        if (root == null) {
            System.out.println("Node with engine number " + engineNumber + " not found.");
            return null;
        }

        if (engineNumber.compareTo(root.stock.getEngineNumber()) < 0) {
            root.left = deleteRec(root.left, engineNumber);
        } else if (engineNumber.compareTo(root.stock.getEngineNumber()) > 0) {
            root.right = deleteRec(root.right, engineNumber);
        } else {
            if (root.left == null && root.right == null) {
                return null;
            }
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            root.stock = minValueNode(root.right).stock;
            root.right = deleteRec(root.right, root.stock.getEngineNumber());
        }

        return root;
    }

    private StockNode minValueNode(StockNode root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    // Inorder traversal to get the sorted stock list as a List
    public List<Stock> inorderTraversal() {
        List<Stock> stockList = new ArrayList<>();
        inorderRec(root, stockList);
        return stockList;
    }

    private void inorderRec(StockNode root, List<Stock> stockList) {
        if (root != null) {
            inorderRec(root.left, stockList);
            stockList.add(root.stock);
            inorderRec(root.right, stockList);
        }
    }

    // Search for a stock by engine number and return the stock if found
    public Stock searchByEngineNumber(String engineNumber) {
        return searchByEngineNumberRec(root, engineNumber);
    }

    private Stock searchByEngineNumberRec(StockNode root, String engineNumber) {
        if (root == null) {
            return null;
        }

        int comparison = engineNumber.compareTo(root.stock.getEngineNumber());
        if (comparison == 0) {
            return root.stock;
        }

        if (comparison < 0) {
            return searchByEngineNumberRec(root.left, engineNumber);
        } else {
            return searchByEngineNumberRec(root.right, engineNumber);
        }
    }
}