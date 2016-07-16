package com.coding4fun.models;

/**
 * Created by coding4fun on 15-Jul-16.
 */

public class GifRowModel {

    private String /*path,*/ name, link;

    public GifRowModel(String name) {
        //this.path = path;
        //this.name = getFileNameWithoutExtension(new File(path));
        this.name = name;
        this.link = "http://www.coding4fun.96.lt/gif/"+name+".gif";
    }

    /*private String getFileNameWithoutExtension(File f){
        String name = f.getName().substring(0, f.getName().lastIndexOf("."));
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}