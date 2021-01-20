package com.agnet.uza.models;

import com.google.gson.annotations.SerializedName;

public class TopProduct {

   int id, totalQnty;
   String totalAmount, margin, name;

   public TopProduct(int id, int totalQnty, String name,String totalAmount, String margin){
       this.id = id;
       this.totalQnty = totalQnty;
       this.totalAmount = totalAmount;
       this.margin = margin;
       this.name = name;
   }

    public int getId() {
        return id;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getName() {
        return name;
    }

    public int getTotalQnty() {
        return totalQnty;
    }

    public String getMargin() {
        return margin;
    }
}
