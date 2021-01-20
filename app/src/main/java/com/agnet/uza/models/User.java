package com.agnet.uza.models;

public class User {

    private int id;
    private String phone;
    private String name;


    public User(int id, String phone,String name) {
        this.id = id;
        this.phone = phone;
        this.name = name;
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
}
