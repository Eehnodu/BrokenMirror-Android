package com.example.brokenmirror.sharedpref;

import android.content.Context;

import com.example.brokenmirror.data.FriendRoomDto;

public class FriendInfoPref extends SharedPreferencesHelper<FriendRoomDto> {
    private static final String prefName = "friend_info";

    public FriendInfoPref(Context context) {
        super(context, prefName);
    }

    public void putFriendInfo(FriendRoomDto friend) {
        putData(prefName, friend);
    }

    public FriendRoomDto getFriendInfo() {
        return getData(prefName, FriendRoomDto.class);
    }

    public void removeFriendInfo() {
        removeData(prefName);
    }

    public void clearFriendInfo() {
        clearAllData();
    }
}
