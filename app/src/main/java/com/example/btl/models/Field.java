package com.example.btl.models;


public class Field {
    private String name;
    private String address;
    private String number;
    private int image;
    private double latitude;
    private double longitude;

    public Field(String name, String address, String number, int image, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.number = number;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter và Setter cho các trường
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }

    public int getImage() {
        return image;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
