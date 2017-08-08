package com.coding4fun.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coding4fun on 03-Jul-17.
 */

public class TruckRequest {

    private String date, from, to, company;
    private float rating;
    private boolean inBulk;
    private List<TruckSubRequest> subRequests;
    private String viewStatus; //to related to the API

    public static final String VIEW_STATUS_CLICKED = "CLICKED";
    public static final String VIEW_STATUS_NOT_CLICKED = "NOT_CLICKED";
    public static final String VIEW_STATUS_LOADING = "LOADING";

    public TruckRequest() {
        date = "3-7-2017";
        from = "Sodeco, Beirut";
        to = "Ashrafieh, Beirut";
        company = "Shipping Company 1";
        rating = 4;
        inBulk = false;
        subRequests = new ArrayList<>();
        viewStatus = VIEW_STATUS_NOT_CLICKED;
    }

    public TruckRequest(List<TruckSubRequest> subRequests) {
        date = "3-7-2017";
        from = "Sodeco, Beirut";
        to = "Ashrafieh, Beirut";
        company = "Shipping Company 1";
        rating = 4;
        inBulk = false;
        viewStatus = VIEW_STATUS_NOT_CLICKED;
        this.subRequests = subRequests;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isInBulk() {
        return inBulk;
    }

    public void setInBulk(boolean inBulk) {
        this.inBulk = inBulk;
    }

    public List<TruckSubRequest> getSubRequests() {
        return subRequests;
    }

    public void setSubRequests(List<TruckSubRequest> subRequests) {
        this.subRequests = subRequests;
    }

    public String getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(String viewStatus) {
        this.viewStatus = viewStatus;
    }
}