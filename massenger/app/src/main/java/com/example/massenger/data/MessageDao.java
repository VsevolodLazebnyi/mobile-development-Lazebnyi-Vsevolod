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
    
    @Query("DELETE FROM messages")
    void deleteAll();
}