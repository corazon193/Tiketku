package com.tiketku.model;

import androidx.annotation.NonNull;

public class TiketModel {
    private String id;
    private String title;
    private int price;
    private String description;
    private String urlImage;
    private String category;
    private float rating;
    private boolean favorite;
    private String key; // untuk nyimpan push key dari Firebase

    // Constructor kosong (Wajib untuk Firebase)
    public TiketModel() {
    }

    // Constructor utama (bisa ditambah sesuai kebutuhan)
    public TiketModel(String id, String title, int price, String description, String urlImage) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.urlImage = urlImage;
    }

    // Getter & Setter semua field
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    // toString untuk debug (opsional)
    @NonNull
    @Override
    public String toString() {
        return title + " - Rp" + price;
    }
}
