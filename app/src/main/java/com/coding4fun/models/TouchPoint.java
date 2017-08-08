package com.coding4fun.models;

/**
 * Created by coding4fun on 15-Sep-16.
 */

public class TouchPoint {

    float x,y;

    public TouchPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}