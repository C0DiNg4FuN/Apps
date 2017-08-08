package com.coding4fun.models;

/**
 * Created by coding4fun on 03-Jul-17.
 */

public class TruckSubRequest {

    private String container, dateTimeFrom, dateTimeTo, commodity;
    private float price, weight;

    public TruckSubRequest() {
        container = "2.0";
        dateTimeFrom = "2017-5-1 13:00 pm";
        dateTimeTo= "2017-5-1 13:00 pm";
        commodity = "whatever";
        price = 100f;
        weight = 1000f;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getDateTimeFrom() {
        return dateTimeFrom;
    }

    public void setDateTimeFrom(String dateTimeFrom) {
        this.dateTimeFrom = dateTimeFrom;
    }

    public String getDateTimeTo() {
        return dateTimeTo;
    }

    public void setDateTimeTo(String dateTimeTo) {
        this.dateTimeTo = dateTimeTo;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}