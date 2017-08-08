package com.coding4fun.models;

import com.google.firebase.database.Exclude;

/**
 * Created by coding4fun on 25-Dec-16.
 */

public class Order {

    private String userID,restaurantID,key,date_time;
    private double totalPrice;
    private boolean responded,delivered;
    private OrderLocation orderLocation;

    public Order() {}

    public Order(String userID, String restaurantID) {
        this.userID = userID;
        this.restaurantID = restaurantID;
        date_time = "31-12-2016 5:55 pm";
        this.totalPrice = 0d;
        responded = false;
        delivered = false;
        orderLocation = new OrderLocation("OrderLocationTitle","OrderLocationPlace","OrderLocationAddress",12.2f,15.5f);
    }

    public String getUserID() {return userID;}

    public void setUserID(String userID) {this.userID = userID;}

    public String getRestaurantID() {return restaurantID;}

    public void setRestaurantID(String restaurantID) {this.restaurantID = restaurantID;}

    public String getDate_time() {return date_time;}

    public void setDate_time(String date_time) {this.date_time = date_time;}

    public double getTotalPrice() {return totalPrice;}

    public void setTotalPrice(double totalPrice) {this.totalPrice = totalPrice;}

    public boolean isResponded() {return responded;}

    public void setResponded(boolean responded) {this.responded = responded;}

    public boolean isDelivered() {return delivered;}

    public void setDelivered(boolean delivered) {this.delivered = delivered;}

    public OrderLocation getOrderLocation() {return orderLocation;}

    public void setOrderLocation(OrderLocation orderLocation) {this.orderLocation = orderLocation;}

    @Exclude
    public String getKey() {return key;}

    @Exclude
    public void setKey(String key) {this.key = key;}

}