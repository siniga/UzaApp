 package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class Product {

    private  int id, discount, stock;
    private String name, cost, imgUrl, barcode, category, sku;
    private double price;

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("server_id")
    private int serverId;

    @SerializedName("sku_id")
    private int skuId;


    public Product(int id, String name, double price, String cost, String barcode,
                   int discount, int stock, String imgUrl, int categoryId, String category, String sku, int serverId, int skuId) {
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
        this.sku = sku;
        this.serverId = serverId;
        this.skuId = skuId;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
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

    public String getSku() {
        return sku;
    }

    public int getServerId() {
        return serverId;
    }

    public int getSkuId() {
        return skuId;
    }
}

