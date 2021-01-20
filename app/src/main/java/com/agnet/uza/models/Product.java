package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class Product {

    private  int id, discount, stock, categoryId;
    private String name, price, cost, imgUrl, barcode, category;


    public Product(int id, String name, String price, String cost, String barcode, int discount, int stock, String imgUrl, int categoryId, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.categoryId = categoryId;
        this.cost = cost;
        this.discount = discount;
        this.stock = stock;
        this.barcode = barcode;
        this.category = category;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCost() {
        return cost;
    }

    public int getStock() {
        return stock;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getBarcode() {
        return barcode;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getDiscount() {
        return discount;
    }

    public String getCategory() {
        return category;
    }
}

