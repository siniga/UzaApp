package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class User {

    private int id;
    private String phone;
    private String name;
    private int syncStatus;

    @SerializedName("server_id")
    private int serverId;

    @SerializedName("deleted_status")
    private int deletedStatus;


    public User(int id, String phone,String name, int syncStatus, int serverId, int deletedStatus) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.syncStatus = syncStatus;
        this.serverId = serverId;
        this.deletedStatus = deletedStatus;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public int getServerId() {
        return serverId;
    }

    public int getDeletedStatus() {
        return deletedStatus;
    }
}
