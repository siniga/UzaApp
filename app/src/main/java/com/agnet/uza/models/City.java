package com.agnet.uza.models;

public class City {

    int id;
    String  name;


    public City(int id, String name){
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
