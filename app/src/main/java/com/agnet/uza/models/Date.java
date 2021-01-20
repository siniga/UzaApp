package com.agnet.uza.models;

import java.util.List;

public class Date {

    private List<Transaction> transactions;
    private boolean isDate;
    private String date;


    public Date(List<Transaction> transactions, boolean isDate, String date) {
        this.transactions = transactions;
        this.isDate = isDate;
        this.date = date;

    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getDate() {
        return date;
    }

    public boolean getIsDate() {
        return isDate;
    }


}
