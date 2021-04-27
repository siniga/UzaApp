package com.agnet.uza.models;

public class Address {

    int id, serverId;
    String name, city, country;


    public Address(int id, String name, String city, String country, int serverId) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.serverId = serverId;
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

    public int getServerId() {
        return serverId;
    }
}
