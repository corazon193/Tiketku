package com.tiketku.model;

public class UserModel {
    public String name, email, alamat, kota, provinsi, telpon, postCode;

    public UserModel() {
        // dibutuhkan untuk Firebase
    }

    public UserModel(String name, String email, String alamat, String kota,
                     String provinsi, String telpon, String postCode) {
        this.name = name;
        this.email = email;
        this.alamat = alamat;
        this.kota = kota;
        this.provinsi = provinsi;
        this.telpon = telpon;
        this.postCode = postCode;
    }
}
