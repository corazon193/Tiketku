package com.tiketku.shared;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveShared {
    private final SharedPreferences shared;
    private String userdata = "user_data";
    Context context;
    public SaveShared(Context context) {
        shared = context.getSharedPreferences(userdata, Context.MODE_PRIVATE);
    }

    public void setKey(String key, String value){
        SharedPreferences.Editor edit = shared.edit();
        edit.putString(key, value);
        edit.apply();
    }
    public String getKey(String key){
        return shared.getString(key, "");
    }

}
