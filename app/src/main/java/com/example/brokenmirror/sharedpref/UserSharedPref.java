package com.example.brokenmirror.sharedpref;

import android.content.Context;

import com.example.brokenmirror.data.UserDto;

public class UserSharedPref extends SharedPreferencesHelper<UserDto> {
    private static final String prefName = "user";

    public UserSharedPref(Context context) {
        super(context, prefName);
    }

    public void putUser(UserDto user) {
        putData(prefName, user);
    }

    public UserDto getUser() {
        return getData(prefName, UserDto.class);
    }

    public void removeUser() {
        removeData(prefName);
    }

    public void clearUser() {
        clearAllData();
    }
}
