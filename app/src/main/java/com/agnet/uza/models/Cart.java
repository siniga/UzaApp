package com.agnet.uza.models;

public class Cart {

    private int id;
    private double totalAmount, originalPrice;
    private int productId, totalQnty;
    private String productName;

   public Cart(int id, double totalAmount, int totalQnty, int productId, String productName, double originalPrice){
       this.id = id;
       this.totalAmount = totalAmount;
       this.totalQnty = totalQnty;
       this.productId = productId;
       this.productName = productName;
       this.originalPrice = originalPrice;
   }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getTotalQnty() {
        return totalQnty;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getProductName() {
        return productName;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }
}

