package com.example.brokenmirror.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public abstract class SharedPreferencesHelper<T> {
    protected final SharedPreferences sharedPref;
    protected final SharedPreferences.Editor editor;
    protected final Gson gson;

    protected SharedPreferencesHelper(Context context, String prefName) {
        this.sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        this.editor = sharedPref.edit();
        this.gson = new Gson();
    }

    public void putData(String prefName, T data) {
        String json = gson.toJson(data);
        editor.putString(prefName, json);
        editor.apply();
    }

    public T getData(String preName, Class<T> classT) {
        String json = sharedPref.getString(preName, null);
        if (json != null && json.isEmpty()) return null;
        return gson.fromJson(json, classT);
    }

    public void removeData(String prefName){
        editor.remove(prefName);
        editor.apply();
    }

    public void clearAllData() {
        editor.clear();
        editor.apply();
    }
}
