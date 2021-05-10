package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class BusinessExpenseCategory {
    private int id;
    private String amount;
    private int businessId, categoryId;

    public BusinessExpenseCategory(int id,String amount, int businessId, int categoryId) {
        this.id = id;
       this.amount = amount;
       this.businessId = businessId;
       this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public int getBusinessId() {
        return businessId;
    }

    public int getCategoryId() {
        return categoryId;
    }

}
