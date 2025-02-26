package com.imperionite.inventorysystem;

public class StockNode {
    Stock stock;
    StockNode left;
    StockNode right;

    public StockNode(Stock stock) {
        this.stock = stock;
        this.left = null;
        this.right = null;
    }
}