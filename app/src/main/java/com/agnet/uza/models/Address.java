package com.agnet.uza.models;

public class Address {

    int id, syncStatus;
    String name, city, country;


    public Address(int id, String name, String city, String country, int syncStatus) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.syncStatus = syncStatus;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public int getSyncStatus() {
        return syncStatus;
    }
}
