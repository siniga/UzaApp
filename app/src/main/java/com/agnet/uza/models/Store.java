package com.agnet.uza.models;

public class Store {

    private int id;
    private String phone;
    private String name;
    private String location;


    public Store(int id, String phone, String name,String location) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
