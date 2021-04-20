package com.agnet.uza.models;

public class User {

    private int id;
    private String phone;
    private String name;
    private int syncStatus;


    public User(int id, String phone,String name, int syncStatus) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.syncStatus = syncStatus;
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

    public int getSyncStatus() {
        return syncStatus;
    }
}
