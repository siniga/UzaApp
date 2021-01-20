package com.agnet.uza.models;

public class ChildTransaction {

    private int id;
    private String TotalAmount, time;

    public ChildTransaction(int id, String TotalAmount, String time){

        this.id = id;
        this.TotalAmount = TotalAmount;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }
}
