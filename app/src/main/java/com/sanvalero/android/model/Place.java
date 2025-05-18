package com.sanvalero.android.model;

public class Place {
    private String name;
    private String location;

    public Place (String name, String location) {
        this.name = name;
        this.location = location;
    }

    // Getters y setters
    public String getName() { return name; }
    public String getLocation() { return location; }
}
