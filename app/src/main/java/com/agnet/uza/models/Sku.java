package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class Sku {

    private int id;
    private String  name;

    @SerializedName("server_id")
    private int serverId;

    @SerializedName("avalability_status")
    private int availabilityStatus;


    public Sku(int id, String name, String imgUrl, int serverId, int availabilityStatus){
        this.id = id;
        this.name = name;
        this.serverId = serverId;
        this.availabilityStatus = availabilityStatus;
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

    public int getAvailabilityStatus() {
        return availabilityStatus;
    }
}
