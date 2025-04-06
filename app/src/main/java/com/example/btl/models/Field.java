package com.example.btl.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Field implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("field_id")
    private int field_id;

    @SerializedName("name")
    private String name;

    @SerializedName("location")
    private String location;

    @SerializedName("type")
    private String type;

    @SerializedName("price")
    private double price;

    @SerializedName("capacity")
    private int capacity;

    @SerializedName("description")
    private String description;

    @SerializedName("images")
    private String images;

    // Constructor mặc định
    public Field() {}

    // Constructor đầy đủ
    public Field(int field_id, String name, String location, String type, double price, int capacity, String description, String images) {
        this.field_id = field_id;
        this.name = name;
        this.location = location;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.images = images;
    }

    // Getter và Setter cho các trường
    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
