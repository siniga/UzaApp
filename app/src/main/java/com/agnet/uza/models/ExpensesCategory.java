package com.agnet.uza.models;

public class ExpensesCategory {

    private int id;
    private String name, imgUrl;

    public ExpensesCategory(int id, String name, String imgUrl){
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
