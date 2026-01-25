package com.example.massenger.model;

import com.google.gson.annotations.SerializedName;

public class ApiMessage {
    @SerializedName("id")
    public Integer apiId;
    public String title;
    public String body;
    @SerializedName("userId")
    public Long userId;
    public Message toMessage() {
        return new Message(String.valueOf(apiId), title, body, userId);
    }
}