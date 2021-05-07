package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Success {
    private User user;

    private List<User> users;

    private List<Address> addresses;

    private List<Outlet> outlets;

    private List<Product> products;

    private List<Category> categories;

    private List<Sku> skus;

    private List<History> orders;

    private List<Partner> partners;

    private String token;

    private Business business;

    private Product product;

    @SerializedName("address")
    private Address address;

    private Category category;
    private Sku sku;


    public Success(List streets, List products, List outlets, List categories, List skus, List<History> orders,
                   List<Partner> partners, List<User> users, User user, String token, Business business,
                   Address address, Product product, Category category, Sku  sku) {

        this.addresses = streets;
        this.products = products;
        this.outlets = outlets;
        this.categories = categories;
        this.skus = skus;
        this.orders = orders;
        this.partners = partners;
        this.user = user;
        this.token = token;
        this.business = business;
        this.address = address;
        this.users = users;
        this.product =  product;
        this.category = category;
        this.sku =  sku;
    }

    public User getUser() {
        return user;
    }

    public List<Partner> getPartners() {
        return partners;
    }

    public List<Address> getAddresses() {
        return addresses;
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

    public String getToken() {
        return token;
    }

    public Business getBusiness() {
        return business;
    }

    public Address getAddress() {
        return address;
    }

    public List<User> getUsers() {
        return users;
    }

    public Product getProduct() {
        return product;
    }

    public Category getCategory() {
        return category;
    }

    public Sku getSku() {
        return sku;
    }
}
