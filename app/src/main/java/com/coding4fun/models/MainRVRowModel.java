package com.coding4fun.models;

/**
 * Created by coding4fun on 13-Jul-16.
 */

public class MainRVRowModel {

    private String title,description;
    private int image;
    private Class<?> _class;

    public MainRVRowModel(String title, String description, int image, Class<?> _class) {
        this.title = title;
        this.image = image;
        this.description = description;
        this._class = _class;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Class<?> get_class() {
        return _class;
    }

    public void set_class(Class<?> _class) {
        this._class = _class;
    }
}