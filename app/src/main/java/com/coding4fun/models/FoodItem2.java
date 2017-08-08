package com.coding4fun.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by coding4fun on 25-Oct-16.
 */

public class FoodItem2 {

    private String name,categoryKey,description;
    private Map<String,Boolean> ingredientsKeys = new HashMap<>();
    @Exclude
    private String key;
    @Exclude
    private Category category;
    @Exclude
    private List<Ingredient> ingredients = new ArrayList<>();

    public FoodItem2() {}

    public FoodItem2(String name, String categoryKey, String description) {
        this.name = name;
        this.categoryKey = categoryKey;
        this.description = description;
    }

    public FoodItem2(String name, String categoryKey, String description, Map<String,Boolean> ingredientsKeys) {
        this.name = name;
        this.categoryKey = categoryKey;
        this.description = description;
        this.ingredientsKeys = ingredientsKeys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Boolean> getIngredientsKeys() {
        return ingredientsKeys;
    }

    public void setIngredientsKeys(Map<String, Boolean> ingredientsKeys) {
        this.ingredientsKeys = ingredientsKeys;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    private void getCategoryObjectByKey(String key){
        FirebaseDatabase.getInstance().getReference("categories").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

}