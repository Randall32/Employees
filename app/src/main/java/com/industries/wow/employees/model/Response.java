package com.industries.wow.employees.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("response")
    private List<Employee> data;

    public Response(){}

    public List<Employee> getData() {
        return data;
    }
}
