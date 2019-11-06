package com.example.smtm7.DataBase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceBase{
    public static final String PREFERENCE_NAME = "data";

    public static SharedPreferenceBase base = null;

    public static SharedPreferenceBase getInstance(Context context){
        if(base==null){
            base = new SharedPreferenceBase(context);
        }
        return base;
    }

    private SharedPreferences preferences;

    public SharedPreferenceBase(Context context){
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();

        //이미 있으면 update
        if(!getString(key).equals("")){
            removeKey(key);
        }

        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        String value = preferences.getString(key, "");
        return value;
    }

    public void removeKey(String key) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(key);
        edit.commit();
    }
}
