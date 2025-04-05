package com.example.btl.models;

public class Discount {
    private int imageResId;
    private int fieldId;

    public Discount(int imageResId, int fieldId) {
        this.imageResId = imageResId;
        this.fieldId = fieldId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getFieldId() {
        return fieldId;
    }
}

