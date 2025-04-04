package com.example.btl.models;

public class RegisterResponse {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public User getUser() {
        return message != null ? message.getData() : null;
    }

    public static class Message {
        private User data;

        public User getData() {
            return data;
        }
    }
}