package com.example.brokenmirror.data;

import java.util.List;

public class ChatRoomDto {
    private String roomId;
    private String roomName;
    private String lastMessage;
    private String lastMessageTimestamp;


    public ChatRoomDto(String roomid, String roomName, String lastMessage, String lastDate) {
        this.roomId = roomid;
        this.roomName = roomName;
        this.lastMessage = lastMessage;
        this.lastMessageTimestamp = lastDate;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(String lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

}
