package com.example.brokenmirror.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.io.Serializable;

@Entity(tableName = "FriendRoom", primaryKeys = {"userId1", "userId2"})
public class FriendRoomDto implements Serializable {
    @NonNull
    private String userId1;
    @NonNull
    private String userId2;
    private String userName;
    private String birth;
    private String phoneNum;
    private String profileImg;
    private int key;

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId2() {
        return userId2;
    }

    public void setUserId2(String userId2) {
        this.userId2 = userId2;
    }

    public String getUserId1() {
        return userId1;
    }

    public void setUserId1(String userId1) {
        this.userId1 = userId1;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public FriendRoomDto(String userId1, String userId2, String userName, String birth, String phoneNum, String profileImg, int key) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.userName = userName;
        this.birth = birth;
        this.phoneNum = phoneNum;
        this.profileImg = profileImg;
        this.key = key;
    }

    public FriendRoomDto() {
    }
}
