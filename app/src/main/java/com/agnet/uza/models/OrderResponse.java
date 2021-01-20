package com.agnet.uza.models;

public class OrderResponse {
    History order;
    int code;



    public OrderResponse(History order, int code){
       this.code = code;
       this.order = order;
    }

    public History getOrder() {
        return order;
    }

    public int getCode() {
        return code;
    }
}
