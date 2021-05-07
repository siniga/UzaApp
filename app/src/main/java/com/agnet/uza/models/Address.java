package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class Address {

    int id;
    String name, city, country;

    @SerializedName("address_server_id")
    int serverId;


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
