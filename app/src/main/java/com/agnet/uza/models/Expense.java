package com.agnet.uza.models;

public class Expense {
    private String name;
    private  String amount;
    private int id, count;

    public Expense(int id ,String name, String amount, int count){
        this.id = id;
        this.name  = name;
        this.amount = amount;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public String getAmount() {
        return amount;
    }
}
