package com.agnet.uza.models;

public class Order {

    int id,status,outletId,customerId;
    String  deviceTime, orderNo,pickUpTime;
    double lat,lng;


    public Order(int id, String name, String deviceTime, String orderNo,int status, String pickUpTime, int outletId, int customerId, double lat, double lng){
        this.id = id;
        this.deviceTime = deviceTime;
        this.orderNo = orderNo;
        this.status = status;
        this.pickUpTime = pickUpTime;
        this.outletId = outletId;
        this.customerId = customerId;
        this.lat = lat;
        this.lng = lng;
    }


    public int getId() {
        return id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public int getOutletId() {
        return outletId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getStatus() {
        return status;
    }

    public String getDeviceTime() {
        return deviceTime;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}

