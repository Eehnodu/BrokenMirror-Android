package com.example.brokenmirror.sharedpref;

import android.content.Context;

public class NotificationSharedPref extends SharedPreferencesHelper<Boolean> {

    // 알림 권한
    public NotificationSharedPref(Context context, String prefName) {
        super(context, prefName);
    }

    public void putNoti(Boolean check, String prefName) {
        putData(prefName, check);
    }

    public Boolean getNoti(String prefName) {
        return getData(prefName, Boolean.class);
    }

    public void removeNoti(String prefName) {
        removeData(prefName);
    }

    public void clearNoti() {
        clearAllData();
    }
}
