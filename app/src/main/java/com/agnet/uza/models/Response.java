package com.agnet.uza.models;

import java.util.List;

public class Response {
    private Success success;
    private int code;


    public Response(Success success, int code) {
        this.success = success;
    }

    public Success getSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }
}
