package com.agnet.uza.models;

public class Store {
    private int id;
    private String name;
    private Street street;

    public Store(int id, String name, Street street){
        this.id = id;
        this.name  = name;
        this.street = street;
    }

    public int getId() {
        return id;
    }

    public Street getStreet() {
        return street;
    }

    public String getName() {
        return name;
    }
}
