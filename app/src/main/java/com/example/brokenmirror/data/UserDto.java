package com.example.brokenmirror.data;

import java.io.Serializable;
import java.util.Date;

public class UserDto implements Serializable {
    private String id;
    private String pw;
    private String userName;
    private String birth;
    private String phoneNum;
    private String profileImg;
    private Date updatedAt;
    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {this.pw = pw;}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getProfileImg() { return profileImg; }

    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }

    public UserDto(String id, String pw, String userName, String birth, String phoneNum, String profileImg) {
        this.id = id;
        this.pw = pw;
        this.userName = userName;
        this.birth = birth;
        this.phoneNum = phoneNum;
        this.profileImg = profileImg;
    }

    public UserDto(String id) {
        this.id = id;
    }

    public UserDto(){ }
}
