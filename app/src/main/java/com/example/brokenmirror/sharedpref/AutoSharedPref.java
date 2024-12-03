package com.example.brokenmirror.sharedpref;

import android.content.Context;

import com.example.brokenmirror.data.UserDto;

public class AutoSharedPref extends SharedPreferencesHelper<Boolean> {
    private static final String prefName = "auto";

    // 자동로그인
    public AutoSharedPref(Context context) {
        super(context, prefName);
    }

    public void putAuto(Boolean auto) {
        putData(prefName, auto);
    }

    public Boolean getAuto() {
        return getData(prefName, Boolean.class);
    }

    public void removeAuto() {
        removeData(prefName);
    }

    public void clearAuto() {
        clearAllData();
    }
}
