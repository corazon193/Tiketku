package com.tiketku.model;

import static com.tiketku.format.DateNow.getDateNow;
import static com.tiketku.format.DateNow.getDayNow;

import androidx.annotation.NonNull;

public class CartModel {
    private String key;
    private String keyProduct;
    private String name;
    private int price;
    private String urlImage;
    private String date;
    private String day;
    private String email;
    private String id, description;

    public CartModel( ){ }

    public CartModel(String key, String id, String keyProduct, String description, String email,String name, int price, String urlImage) {
        this.key = key;
        this.keyProduct = keyProduct;
        this.description = description;
        this.email = email;
        this.name = name;
        this.price = price;
        this.urlImage = urlImage;
        this.date = getDateNow();
        this.day = getDayNow();
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setKeyProduct(String keyProduct) {
        this.keyProduct = keyProduct;
    }

    public String getKey() {
        return key;
    }

    public String getKeyProduct() {
        return keyProduct;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public String getUrlImage() {
        return urlImage;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
