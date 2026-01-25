package com.example.massenger.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String body;
    public Long userId;

    public Long timestamp;

    public Message(@NonNull String id, String title, String body, Long userId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
    }
}