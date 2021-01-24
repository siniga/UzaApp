package com.agnet.uza.helpers;

public class Staff {
    private int id;
    private String name, phone;

    public  Staff(int id, String name, String phone){
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
