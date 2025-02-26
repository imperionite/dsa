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
            root = new StockNode(stock);
            return root;
        }

        if (stock.getBrand().compareTo(root.stock.getBrand()) < 0) {
            root.left = insertRec(root.left, stock);
        } else if (stock.getBrand().compareTo(root.stock.getBrand()) > 0) {
            root.right = insertRec(root.right, stock);
        }

        return root;
    }

    // Inorder traversal to get the sorted stock list
    public void inorder() {
        inorderRec(root);
    }

    private void inorderRec(StockNode root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.println(root.stock); // Print stock details
            inorderRec(root.right);
        }
    }

    // Search for a stock by brand
    public Stock searchByBrand(String brand) {
        return searchByBrandRec(root, brand);
    }

    private Stock searchByBrandRec(StockNode root, String brand) {
        if (root == null || root.stock.getBrand().equals(brand)) {
            return root != null ? root.stock : null;
        }

        if (brand.compareTo(root.stock.getBrand()) < 0) {
            return searchByBrandRec(root.left, brand);
        }

        return searchByBrandRec(root.right, brand);
    }

    // Implement other methods like delete if needed (similar logic)
}
