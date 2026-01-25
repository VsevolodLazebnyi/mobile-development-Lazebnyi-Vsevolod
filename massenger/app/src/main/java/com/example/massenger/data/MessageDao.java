package com.example.massenger.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.massenger.model.Message;
import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    LiveData<List<Message>> getAll();

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    List<Message> getAllSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Message... messages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Message message);

    @Query("DELETE FROM messages")
    void deleteAll();

    @Query("UPDATE messages SET isLiked = :isLiked, likeCount = :likeCount WHERE id = :id")
    void updateLike(String id, boolean isLiked, int likeCount);

    @Query("SELECT * FROM messages WHERE id = :id")
    Message getMessageById(String id);
}