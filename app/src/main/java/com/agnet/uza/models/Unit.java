package com.agnet.uza.models;

public class Unit {

    int id, serverId;
    String  name;



    public Unit(int id, String name, String imgUrl, int serverId){
        this.id = id;
        this.name = name;
        this.serverId = serverId;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServerId() {
        return serverId;
    }


    @Override
    public String toString() {
        return name;
    }
}
