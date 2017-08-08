package com.coding4fun.models;

import com.google.firebase.database.Exclude;

/**
 * Created by coding4fun on 29-Oct-16.
 */

public class Ingredient {

    private String name;
    @Exclude
    private String key;

    public Ingredient(){}

    public Ingredient(String name){
        this.name = name;
    }

    public Ingredient(String key, String name){
        this.key= key;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getKey() {return key;}

    public void setKey(String key) {this.key = key;}
}