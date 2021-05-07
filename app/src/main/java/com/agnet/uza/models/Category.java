package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class Category {

    private int id;
    private String name, photo;

    @SerializedName("avalability_status")
    private int availabilityStatus;

    @SerializedName("server_id")
    private int serverId;

    public Category(int id, String name, String photo, int serverId, int availabilityStatus) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.serverId = serverId;
        this.availabilityStatus = availabilityStatus;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return photo;
    }

    public int getServerId() {
        return serverId;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getAvailabilityStatus() {
        return availabilityStatus;
    }
}
