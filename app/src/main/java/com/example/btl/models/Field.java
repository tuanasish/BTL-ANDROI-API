
package com.example.btl.models;


import com.google.gson.annotations.SerializedName;

public class Field {
    @SerializedName("field_id")
    private int field_id;

    @SerializedName("name")
    private String name;

    @SerializedName("location")
    private String location;

    @SerializedName("type")
    private String type;

    @SerializedName("price")
    private String price;

    @SerializedName("capacity")
    private int capacity;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String images;

    public Field() {
    }

    public Field(Integer capacity, String description, int field_id, String images, String location, String name, double price, String type) {
        this.capacity = capacity;
        this.description = description;
        this.field_id = field_id;
        this.images = images;
        this.location = location;
        this.name = name;
        this.price = String.valueOf(price);
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return Double.parseDouble(price);
    }

    public void setPrice(double price) {
        this.price = String.valueOf(price);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
