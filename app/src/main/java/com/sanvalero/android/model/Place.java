package com.sanvalero.android.model;

import java.time.LocalDate;

public class Place {
    private Long id;
    private String name;
    private String address;
    private Integer capacity;
    private Double area;
    private String inaugurationDate;
    private Boolean hasParking;
    private String equipment;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getInaugurationDate() {
        return inaugurationDate;
    }

    public void setInaugurationDate(String inaugurationDate) {
        this.inaugurationDate = inaugurationDate;
    }

    public Boolean getHasParking() {
        return hasParking;
    }

    public void setHasParking(Boolean hasParking) {
        this.hasParking = hasParking;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
}