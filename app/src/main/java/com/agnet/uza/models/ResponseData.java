package com.agnet.uza.models;

import java.util.List;

public class ResponseData {
    List<Street> streets;
    List<Outlet> outlets;

   // @SerializedName("data")
    List<Product> products;

    List<Category> categories;

    List<Sku> skus;

    List<History> orders;

    List<Partner> partners;

    public ResponseData(List streets, List products, List outlets, List categories, List skus, List<History> orders, List<Partner> partners){
       this.streets = streets;
       this.products = products;
       this.outlets = outlets;
       this.categories = categories;
       this.skus = skus;
       this.orders = orders;
       this.partners = partners;
    }


    public List<Partner> getPartners() {
        return partners;
    }

    public List<Street> getStreets() {
        return streets;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Outlet> getOutlets() {
        return outlets;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public List<History> getOrders() {
        return orders;
    }
}
