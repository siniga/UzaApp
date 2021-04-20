package com.agnet.uza.models;

public class Order {

    int id,status;
    String  deviceTime, orderNo;


    public Order(int id, String deviceTime, String orderNo, int status){
        this.id = id;
        this.deviceTime = deviceTime;
        this.orderNo = orderNo;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getDeviceTime() {
        return deviceTime;
    }

    public int getStatus() {
        return status;
    }
}

