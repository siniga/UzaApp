package com.agnet.uza.models;

public class ExpensesCategory {

    private int id;
    private String name, amount;

    public ExpensesCategory(int id, String name, String amount){
        this.id = id;
        this.name = name;
        this.amount = amount;
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
}
