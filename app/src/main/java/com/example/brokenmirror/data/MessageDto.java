package com.example.brokenmirror.data;

import java.time.ZonedDateTime;

public class MessageDto {
    private String roomId;
    private String message;
    private String sender;
    private String receiver;
    //private Instant timestamp;

    public MessageDto(String roomId, String message, String sender, String receiver) {
        this.roomId = roomId;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        //this.timestamp = timestamp;
    }


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

//    public Instant getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Instant timestamp) {
//        this.timestamp = timestamp;
//    }


    @Override
    public String toString() {
        return "MessageDto{" +
                "roomId='" + roomId + '\'' +
                ", message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
//            ", timestamp=" + timestamp +
                '}';
    }
}
