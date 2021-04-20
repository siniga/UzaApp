package com.agnet.uza.models;

public class BusinessType {

    private int id;
    private String name;

    public BusinessType(int id, String name){
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


