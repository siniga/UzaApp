package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class History {

    int id, status;

    @SerializedName("order_no")
    String orderNo;

    @SerializedName("created_date")
    String  createdAt;

    @SerializedName("device_order_time")
    String  orderTime;


    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public int getStatus() {
        return status;
    }
}
