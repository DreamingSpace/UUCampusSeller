package com.dreamspace.uucampusseller.model;

/**
 * Created by wufan on 2015/10/21.
 */
public class OrderItem {

    @Override
    public String toString() {
        return "OrderItem{"+"name"+"}";
    }

    private String image;
    private String name;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
