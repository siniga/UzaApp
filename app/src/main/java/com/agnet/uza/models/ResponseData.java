package com.agnet.uza.models;

import java.util.List;

public class ResponseData {
    private User user;

    private List<Street> streets;

    private List<Outlet> outlets;

    private List<Product> products;

    private List<Category> categories;

    private List<Sku> skus;

    private List<History> orders;

    private List<Partner> partners;

    private int code;

    private String token;

    private Business business;


    public ResponseData(List streets, List products, List outlets, List categories, List skus, List<History> orders, List<Partner> partners, User user, int code, String token, Business business) {
        this.streets = streets;
        this.products = products;
        this.outlets = outlets;
        this.categories = categories;
        this.skus = skus;
        this.orders = orders;
        this.partners = partners;
        this.user = user;
        this.code = code;
        this.token = token;
        this.business = business;
    }

    public User getUser() {
        return user;
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

    public int getCode() {
        return code;
    }

    public String getToken() {
        return token;
    }

    public Business getBusiness() {
        return business;
    }
}
