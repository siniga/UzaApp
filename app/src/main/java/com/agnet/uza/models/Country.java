package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class Country {

    private int id;

    @SerializedName("country_name")
    private String name;

    public Country(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
