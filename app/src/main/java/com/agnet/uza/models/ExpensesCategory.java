package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class ExpensesCategory {

    private int id;
    private String name, imgUrl;

    @SerializedName("server_id")
    private int serverId;

    public ExpensesCategory(int id, String name, String imgUrl, int serverId){
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.serverId = serverId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getServerId() {
        return serverId;
    }
}
