package com.coding4fun.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by coding4fun on 25-Oct-16.
 */

public class FoodItem {

    private String name,category,description;
    private Map<String,Boolean> ingredients = new HashMap<>();
    @Exclude
    private List<Ingredient> ingredientsList = new ArrayList<>();
    @Exclude
    private String key;

    public FoodItem() {}

    public FoodItem(String name, String category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public FoodItem(String name, String category, String description, List<Ingredient> ingredientsList) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.ingredientsList = ingredientsList;
        for(Ingredient i : ingredientsList) this.ingredients.put(i.getKey(),true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public Map<String, Boolean> getIngredients() {return ingredients;}

    public void setIngredients(Map<String, Boolean> ingredients) {this.ingredients = ingredients;}

    @Exclude
    public List<Ingredient> getIngredientsList() {return ingredientsList;}

    public void setIngredientsList(List<Ingredient> ingredientsList) {this.ingredientsList = ingredientsList;}

    @Exclude
    public String getKey() {return key;}

    public void setKey(String key) {this.key = key;}

    public void addIngredient(Ingredient ingredient){
        this.ingredientsList.add(ingredient);
        this.ingredients.put(ingredient.getKey(),true);
    }

}