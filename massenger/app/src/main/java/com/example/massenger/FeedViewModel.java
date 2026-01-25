package com.example.massenger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.massenger.model.Message;
import com.example.massenger.repository.MessageRepository;
import java.util.List;

public class FeedViewModel extends ViewModel {
    private MessageRepository repository;
    private LiveData<List<Message>> messages;

    public void initRepository(android.content.Context context) {
        if (repository == null) {
            repository = new MessageRepository(context.getApplicationContext());
        }
    }

    public LiveData<List<Message>> getMessages() {
        if (messages == null && repository != null) {
            messages = repository.getMessages();
        }
        return messages;
    }

    public LiveData<List<Message>> refreshMessages() {
        if (repository != null) {
            messages = repository.refreshMessages();
        }
        return messages;
    }
}