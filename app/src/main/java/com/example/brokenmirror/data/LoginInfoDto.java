package com.example.brokenmirror.data;

import java.util.Date;

public class LoginInfoDto {
    private Long loginIdx;
    private String id;
    private String device;
    private String ipAddress;
    private Date createdAt;

    public Long getLoginIdx() {
        return loginIdx;
    }

    public void setLoginIdx(Long loginIdx) {
        this.loginIdx = loginIdx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public LoginInfoDto(Long loginIdx, String id, String device, String ipAddress, Date createdAt) {
        this.loginIdx = loginIdx;
        this.id = id;
        this.device = device;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public LoginInfoDto() {
    }
}
