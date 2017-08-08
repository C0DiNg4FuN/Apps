package com.coding4fun.models;

/**
 * Created by coding4fun on 28-Dec-16.
 */

public class OrderLocation {

    private String title,place,address; //place is google maps determined place. address is user description
    private float lat,lng;

    public OrderLocation() {}

    public OrderLocation(String title, String place, String address, float lat, float lng) {
        this.title = title;
        this.place = place;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }
}