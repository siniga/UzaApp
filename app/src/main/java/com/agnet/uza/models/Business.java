package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class Business {
    private int id;
    private String name;

    @SerializedName("address_id")
    private int streetId;

    public Business(int id, String name, int streetId) {
        this.id = id;
        this.name = name;
        this.streetId = streetId;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public int getStreetId() {
        return streetId;
    }
}
