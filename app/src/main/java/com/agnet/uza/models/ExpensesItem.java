package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class ExpensesItem {

    private int id;
    private String name, amount, date;
    @SerializedName("expense_category_id")
    private int categoryId;

    public ExpensesItem(int id, String name, String amount, String date, int categoryId){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
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

    public int getCategoryId() {
        return categoryId;
    }
}
