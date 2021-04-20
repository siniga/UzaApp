package com.agnet.uza.models;

public class ExpensesItem {

    private int id;
    private String name, amount, date;

    public ExpensesItem(int id, String name, String amount, String date){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
