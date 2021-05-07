package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class Business {
    private int id;
    private String name;

    @SerializedName("address_id")
    private int addressId;

    @SerializedName("business_server_id")
    private int serverId;
    private Address address;

    public Business(int id, String name, int addressId,int serverId) {
        this.id = id;
        this.name = name;
        this.addressId =  addressId;
        this.serverId = serverId;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public Address getAddress() {
        return address;
    }

    public int getServerId() {
        return serverId;
    }
}
