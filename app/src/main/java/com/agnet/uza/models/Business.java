package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class Business {
    private int id;
    private String name;

    @SerializedName("address_id")
    private int addressId;

    public Business(int id, String name, int addressId) {
        this.id = id;
        this.name = name;
        this.addressId =  addressId;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public int getAddressId() {
        return addressId;
    }
}
