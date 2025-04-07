package com.example.btl.models;

import com.google.gson.annotations.SerializedName;

public class FieldSingleResponse {
    @SerializedName("data")
    private Field data;

    public Field getData() {
        return data;
    }

    public void setData(Field data) {
        this.data = data;
    }
}
