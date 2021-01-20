package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class Outlet {

    int id;
    String  name, district, lat, lon, url;

    int serverId;

    @SerializedName("street_id")
    int streetId;


    public Outlet(int id, String name, int streetId, String district, String url, String lat, String lon, int serverId){
        this.id = id;
        this.name = name;
        this.streetId = streetId;
        this.district = district;
        this.url = url;
        this.lat = lat;
        this.lon = lon;
        this.serverId = serverId;
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

    public String getUrl() {
        return url;
    }

    public String getDistrict() {
        return district;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public int getServerId() {
        return serverId;
    }
}
