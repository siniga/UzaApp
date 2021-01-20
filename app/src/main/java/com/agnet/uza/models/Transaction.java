package com.agnet.uza.models;

public class Transaction {

    private int id, totalQnty;
    private String date;

    public Transaction(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
}
