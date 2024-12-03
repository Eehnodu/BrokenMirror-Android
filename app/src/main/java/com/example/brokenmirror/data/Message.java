package com.example.brokenmirror.data;

public class Message {
    private String roomId;
    private String message;
    private String sender;
    private String receiver;
    // private double timestamp;
    private String timestamp;

    // 기본 생성자
    public Message() {
    }

    // 모든 필드를 포함하는 생성자
    public Message(String roomId, String message, String sender, String receiver, String timestamp) {
        this.roomId = roomId;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }

    // Getters and setters
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

//    @Override
//    public String toString() {
//        return "Message{" +
//                "roomId='" + roomId + '\'' +
//                ", message='" + message + '\'' +
//                ", sender='" + sender + '\'' +
//                ", receiver='" + receiver + '\'' +
//                ", timestamp=" + timestamp +
//                '}';
//    }
}

