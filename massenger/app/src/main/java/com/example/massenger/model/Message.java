package com.example.massenger.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "body")
    public String body;

    @ColumnInfo(name = "userId")
    public Long userId;

    @ColumnInfo(name = "timestamp")
    public Long timestamp;

    @ColumnInfo(name = "userName", defaultValue = "Пользователь")
    public String userName;

    @ColumnInfo(name = "avatarUrl")
    public String avatarUrl;

    @ColumnInfo(name = "isLiked", defaultValue = "0")
    public Boolean isLiked = false;

    @ColumnInfo(name = "likeCount", defaultValue = "0")
    public Integer likeCount = 0;

    @ColumnInfo(name = "messageTime")
    public String messageTime;

    public Message(String title, String body, Long userId, String userName, String avatarUrl) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.body = body;
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
        this.messageTime = getCurrentTime();
        this.userName = userName;
        this.avatarUrl = avatarUrl;
        this.isLiked = false;
        this.likeCount = 0;
    }

    public Message(@NonNull String id, String title, String body, Long userId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
        this.messageTime = getCurrentTime();
        this.userName = "Пользователь " + userId;
        this.avatarUrl = "";
        this.isLiked = false;
        this.likeCount = 0;
    }

    public Message() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.messageTime = getCurrentTime();
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}