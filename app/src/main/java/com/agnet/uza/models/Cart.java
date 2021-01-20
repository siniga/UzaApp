package com.agnet.uza.models;

public class Cart {

    private int id, totalQnty;
    private String name, totalPrice, sku, unit, img;
    private int productId;

   public Cart(String name, String totalPrice, String sku, String unit, int totalQnty){
       this.name = name;
       this.totalPrice = totalPrice;
       this.sku = sku;
       this.unit = unit;
       this.totalQnty = totalQnty;
   }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setTotalQnty(int totalQnty) {
        this.totalQnty = totalQnty;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public int getTotalQnty() {
        return totalQnty;
    }

    public int getProductId() {
        return productId;
    }

    public String getSku() {
        return sku;
    }

    public String getImg() {
        return img;
    }

    public String getUnit() {
        return unit;
    }
}
